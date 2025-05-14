package com.java.biao.spring.aop.aspect;

import com.java.biao.spring.aop.intercept.GPMethodInterceptor;
import com.java.biao.spring.aop.intercept.GPMethodInvocation;

import java.lang.reflect.Method;

public class GPMethodBeforeAdviceInterceptor extends GPAbstractAspectAdvice implements GPAdvice, GPMethodInterceptor {

    private GPJoinPoint joinPoint;

    public GPMethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(GPMethodInvocation mi) throws Throwable {
        // joinPoint其实就是方法包含的上下文信息
        this.joinPoint = mi;
        before(mi.getMethod(), mi.getArguments(), mi.getThis());
        return mi.proceed();
    }

    private void before(Method method,Object[] args,Object target) throws Throwable{
        //传送了给织入参数
        super.invokeAdviceMethod(this.joinPoint,null,null);
    }
}
