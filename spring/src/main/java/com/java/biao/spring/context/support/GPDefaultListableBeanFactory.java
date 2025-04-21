package com.java.biao.spring.context.support;

import com.java.biao.spring.config.GPBeanDefinition;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor
public class GPDefaultListableBeanFactory extends GPAbstractApplicationContext {

    protected final Map<String, GPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
}
