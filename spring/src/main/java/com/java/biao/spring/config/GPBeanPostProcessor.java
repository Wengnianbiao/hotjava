package com.java.biao.spring.config;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GPBeanPostProcessor {

    // 在bean实例化之前，提供回调的入口
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    // 在bean实例化之后，提供回调的入口
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
}
