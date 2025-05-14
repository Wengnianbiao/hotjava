package com.java.biao.spring.aop;

/**
 * 获取代理对象的顶层接口即代理工厂
 * 默认使用jdk动态代理
 */
public interface GPAopProxy {

    Object getProxy();

    Object getProxy(ClassLoader classLoader);
}
