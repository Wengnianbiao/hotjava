package com.java.biao.spring.aop.aspect;

import java.lang.reflect.Method;

/**
 * 回调连接点，通过它可以获得被代理业务方法的所有信息
 */
public interface GPJoinPoint {

    // 获取被代理对象
    Object getThis();

    // 获取被代理方法参数
    Object[] getArguments();

    // 获取被代理方法
    Method getMethod();

    // 添加自定义属性
    void setUserAttribute(String key, Object value);

    // 获取自定义属性
    Object getUserAttribute(String key);
}
