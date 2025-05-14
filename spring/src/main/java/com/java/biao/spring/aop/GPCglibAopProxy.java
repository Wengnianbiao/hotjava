package com.java.biao.spring.aop;

import com.java.biao.spring.aop.support.GPAdvisedSupport;

public class GPCglibAopProxy implements  GPAopProxy {
    public GPCglibAopProxy(GPAdvisedSupport config) {
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
