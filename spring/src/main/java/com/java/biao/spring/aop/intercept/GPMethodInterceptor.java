package com.java.biao.spring.aop.intercept;

/**
 * 方法拦截器顶层接口
 */
public interface GPMethodInterceptor {

    Object invoke(GPMethodInvocation invocation) throws Throwable;
}
