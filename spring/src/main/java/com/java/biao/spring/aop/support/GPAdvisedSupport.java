package com.java.biao.spring.aop.support;

import com.alibaba.druid.util.StringUtils;
import com.java.biao.spring.aop.aspect.GPAfterReturningAdviceInterceptor;
import com.java.biao.spring.aop.aspect.GPAfterThrowingAdviceInterceptor;
import com.java.biao.spring.aop.aspect.GPMethodBeforeAdviceInterceptor;
import com.java.biao.spring.aop.config.GPAopConfig;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GPAdvisedSupport {

    // 增强对象的类
    private Class<?> targetClass;

    // 增强对象本身
    private Object target;

    private GPAopConfig config;

    private Pattern pointCutClassPattern;

    // 保存着方法对应的所有拦截器
    private transient Map<Method, List<Object>> methodCache;

    public GPAdvisedSupport(GPAopConfig config) {
        this.config = config;
    }

    public Class<?> getTargetClass() {
        return this.targetClass;
    }

    public Object getTarget() {
        return this.target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    // 获取方法对应的所有拦截器
    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) throws Exception {
        List<Object> cached = methodCache.get(method);
        if (cached == null) {
            // 什么情况下method入参和这里的会有区别呢？？
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());

            cached = methodCache.get(m);

            //底层逻辑，对代理方法进行一个兼容处理
            this.methodCache.put(m, cached);
        }

        return cached;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
        parse();
    }

    private void parse() {
        String pointCut = config.getPointCut()
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");
        // 正则的转化，目的就是把 pointCut 的正则表达式进行匹配
        String pointCutForClassRegex = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
        pointCutClassPattern = Pattern.compile("class " + pointCutForClassRegex.substring(
                pointCutForClassRegex.lastIndexOf(" ") + 1));

        try {

            methodCache = new HashMap<>();
            Pattern pattern = Pattern.compile(pointCut);


            Class<?> aspectClass = Class.forName(this.config.getAspectClass());
            Map<String, Method> aspectMethods = new HashMap<>();
            // 切面类的方法集合
            for (Method m : aspectClass.getMethods()) {
                aspectMethods.put(m.getName(), m);
            }

            for (Method m : this.targetClass.getMethods()) {
                String methodString = m.toString();
                if (methodString.contains("throws")) {
                    methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();
                }
                // 这里的逻辑就是如果方法命中了切点,就把aop中配置的各种通知类型注册为方法拦截器
                Matcher matcher = pattern.matcher(methodString);
                if (matcher.matches()) {
                    //执行器链
                    List<Object> advices = new LinkedList<>();
                    //把切面的每一个方法包装成 MethodInterceptor
                    //如果配置了就生成对应的拦截器
                    if (!StringUtils.isEmpty(config.getAspectBefore())) {
                        //创建一个前置通知Advice
                        GPMethodBeforeAdviceInterceptor gpMethodBeforeAdviceInterceptor = new GPMethodBeforeAdviceInterceptor(
                                aspectMethods.get(config.getAspectBefore()), aspectClass.newInstance());

                        advices.add(gpMethodBeforeAdviceInterceptor);
                    }
                    //after
                    if (!StringUtils.isEmpty(config.getAspectAfter())) {
                        //创建一个后置通知Advice
                        GPAfterReturningAdviceInterceptor gpAfterReturningAdviceInterceptor = new GPAfterReturningAdviceInterceptor(
                                aspectMethods.get(config.getAspectAfter()), aspectClass.newInstance());

                        advices.add(gpAfterReturningAdviceInterceptor);
                    }
                    //afterThrowing
                    if (!StringUtils.isEmpty(config.getAspectAfterThrow())) {
                        //创建一个异常通知Advice
                        GPAfterThrowingAdviceInterceptor throwingAdvice =
                                new GPAfterThrowingAdviceInterceptor(
                                        aspectMethods.get(config.getAspectAfterThrow()),
                                        aspectClass.newInstance());
                        throwingAdvice.setThrowName(config.getAspectAfterThrowingName());

                        advices.add(throwingAdvice);
                    }
                    methodCache.put(m, advices);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public boolean pointCutMatch() {
        // 判断目标类是否符合切面规则,即是否能够命中切面
        return pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }
}
