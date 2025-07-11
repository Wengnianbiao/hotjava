package com.java.biao.spring.aop.aspect;

import com.java.biao.spring.aop.intercept.GPMethodInterceptor;
import com.java.biao.spring.aop.intercept.GPMethodInvocation;

import java.lang.reflect.Method;

/**
 * Created by Tom on 2019/4/15.
 */
public class GPAfterThrowingAdviceInterceptor extends GPAbstractAspectAdvice implements GPAdvice, GPMethodInterceptor {


    private String throwingName;

    public GPAfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(GPMethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        } catch (Throwable e) {
            invokeAdviceMethod(mi, null, e.getCause());
            throw e;
        }
    }

    public void setThrowName(String throwName) {
        this.throwingName = throwName;
    }
}
