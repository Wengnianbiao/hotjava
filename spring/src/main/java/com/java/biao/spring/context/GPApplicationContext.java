package com.java.biao.spring.context;

import com.java.biao.spring.annotation.GPAutowired;
import com.java.biao.spring.annotation.GPController;
import com.java.biao.spring.annotation.GPService;
import com.java.biao.spring.aop.GPAopProxy;
import com.java.biao.spring.aop.GPCglibAopProxy;
import com.java.biao.spring.aop.GPJdkDynamicAopProxy;
import com.java.biao.spring.aop.config.GPAopConfig;
import com.java.biao.spring.aop.support.GPAdvisedSupport;
import com.java.biao.spring.beans.GPBeanWrapper;
import com.java.biao.spring.config.GPBeanDefinition;
import com.java.biao.spring.config.GPBeanPostProcessor;
import com.java.biao.spring.context.support.GPBeanDefinitionReader;
import com.java.biao.spring.context.support.GPDefaultListableBeanFactory;
import com.java.biao.spring.core.GPBeanFactory;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor
public class GPApplicationContext extends GPDefaultListableBeanFactory implements GPBeanFactory {

    private String[] configLocations;
    private GPBeanDefinitionReader reader;

    // 单例的IOC容器缓存
    private Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    // 通用的IOC容器
    private Map<String, GPBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

    public GPApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        try {
            refresh();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void refresh() {
        //1.根据web.xml中配置的路径去扫描编译后中的所有类文件(.class)，获取到所有类文件
        reader = new GPBeanDefinitionReader(this.configLocations);
        //2.遍历所有类，将所有类封装成BeanDefinition
        List<GPBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
        //3.注册，将BeanDefinition注册到容器中，容器本质上就是存着Bean名称到BeanDefinition的映射关系
        doRegisterBeanDefinition(beanDefinitions);
        //4.把不是懒加载的类就提前初始化
        doAutowired();
    }

    private void doAutowired() {

        for (Map.Entry<String, GPBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            // 如果不是懒加载就直接初始化
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void doRegisterBeanDefinition(List<GPBeanDefinition> beanDefinitions) {
        for (GPBeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new RuntimeException("The “" + beanDefinition.getFactoryBeanName() + "” is exists!!");
            }

            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
            // 至此，容器初始化完毕
        }
    }

    // 读取BeanDefinition的配置信息，通过反射机制创建实例对象
    // Spring的做法是用装饰器模式，将bean封装到BeanWrapper中
    // 再通过getBean方法获取到BeanWrapper中的实例对象
    @Override
    public Object getBean(String beanName) throws Exception {
        GPBeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);
        Object instance = instantiateBean(beanDefinition);
        if (instance == null) {
            return null;
        }
        // Bean的前置后置处理器
        GPBeanPostProcessor beanPostProcessor = new GPBeanPostProcessor();
        // 在实例初始化之前调用一次:真正的实例化是在factoryBeanInstanceCache中生成了实例，用的装饰器模式
        beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
        GPBeanWrapper beanWrapper = new GPBeanWrapper(instance);
        this.factoryBeanInstanceCache.put(beanName, beanWrapper);
        // 在实例初始化之后调用一次
        beanPostProcessor.postProcessAfterInitialization(instance, beanName);
        // 完成DI注入
        populateBean(beanWrapper);

        return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
    }

    private void populateBean(GPBeanWrapper gpBeanWrapper) {
        Object instance = gpBeanWrapper.getWrappedInstance();

        Class<?> clazz = gpBeanWrapper.getWrappedClass();
        if (!clazz.isAnnotationPresent(GPController.class) ||
                clazz.isAnnotationPresent(GPService.class)) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(GPAutowired.class)) {
                continue;
            }
            // 获取注解的value属性
            String autowiredBeanName = field.getAnnotation(GPAutowired.class).value().trim();
            if (autowiredBeanName.isEmpty()) {
                autowiredBeanName = field.getType().getName();
            }
            field.setAccessible(true);

            try {
                // 会出现为Null的情况，而且这样还会出现循环依赖的问题，此处暂时不解决这个问题
                if (this.factoryBeanInstanceCache.get(autowiredBeanName) == null) {
                    getBean(autowiredBeanName);
                }
                field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 传入一个BeanDefinition，创建一个BeanWrapper
    private Object instantiateBean(GPBeanDefinition beanDefinition) {
        Object instance;
        String className = beanDefinition.getBeanClassName();
        try {
            if (this.factoryBeanObjectCache.containsKey(className)) {
                instance = this.factoryBeanObjectCache.get(className);
            } else {
                // 因为是需要反射实例化，所以需要类提供无参构造方法
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();

                // 初始化AOP配置信息,去判断需不需要为这个类增强,这里是每个类都需要初始化配置
                // Spring源码不可能这么做吧？
                GPAdvisedSupport aopConfig = initializationAopConfig(beanDefinition);
                // 在这里触发了初始化,这里的思想是不管当前的类是否命中都会去处理加载
                // 是不是没必要？而是先判断有没有命中在进行拦截器配置
                aopConfig.setTargetClass(clazz);
                aopConfig.setTarget(instance);

                //符合PointCut的规则的话，创建代理对象
                if(aopConfig.pointCutMatch()) {
                    instance = createProxy(aopConfig).getProxy();
                }

                // 所以如果是代理对象的话，就直接返回代理对象，否则就返回实例对象
                this.factoryBeanObjectCache.put(className, instance);
                this.factoryBeanObjectCache.put(beanDefinition.getFactoryBeanName(),instance);
            }
            return instance;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.out.println("当前bean:" + className);
            e.printStackTrace();
        }
        return null;
    }

    private GPAopProxy createProxy(GPAdvisedSupport config) {

        Class<?> targetClass = config.getTargetClass();
        // 如果有实现接口就使用JDK动态代理,反之使用CGLIB
        if(targetClass.getInterfaces().length > 0){
            return new GPJdkDynamicAopProxy(config);
        }
        return new GPCglibAopProxy(config);
    }

    private GPAdvisedSupport initializationAopConfig(GPBeanDefinition beanDefinition) {
        GPAopConfig config = new GPAopConfig();
        config.setPointCut(this.reader.getConfig().getProperty("pointCut"));
        config.setAspectClass(this.reader.getConfig().getProperty("aspectClass"));
        config.setAspectBefore(this.reader.getConfig().getProperty("aspectBefore"));
        config.setAspectAfter(this.reader.getConfig().getProperty("aspectAfter"));
        config.setAspectAfterThrow(this.reader.getConfig().getProperty("aspectAfterThrow"));
        config.setAspectAfterThrowingName(this.reader.getConfig().getProperty("aspectAfterThrowingName"));
        return new GPAdvisedSupport(config);
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[0]);
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }
}
