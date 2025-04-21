package com.java.biao.spring.core;

public interface GPBeanFactory {

    /**
     * 根据beanName获取bean
     * @param beanName beanName
     * @return
     * @throws Exception
     */
    Object getBean(String beanName) throws Exception;

    Object getBean(Class<?> beanClass) throws Exception;
}
