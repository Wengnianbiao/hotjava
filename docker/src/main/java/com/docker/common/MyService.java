package com.docker.common;

public class MyService {
    public MyService() {
        System.out.println("MyService 构造方法执行，创建实例");
    }

    public String sayHello() {
        return "Hello from MyService";
    }
}
