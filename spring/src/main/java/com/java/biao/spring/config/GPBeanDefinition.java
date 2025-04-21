package com.java.biao.spring.config;

import lombok.NoArgsConstructor;

// 存储bean的配置信息
@NoArgsConstructor
public class GPBeanDefinition {

    // bean的名称
    private String beanClassName;

    // 是否延时
    private boolean lazyInit = false;
    private String factoryBeanName;

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }
}