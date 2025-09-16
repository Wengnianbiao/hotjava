package com.docker.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.function.Supplier;

@Configuration
public class AppConfig {

    // 工厂方法：创建 MyService 实例
    @Bean
    public MyService myServiceFactory() {
        System.out.println("执行工厂方法 myServiceFactory");
        return new MyService();
    }
}
