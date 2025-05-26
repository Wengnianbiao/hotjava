package com.biao.job.quartzjob.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://127.0.0.1:8080") // 假设前端运行在 8080
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")  // 允许的头
                .allowCredentials(true);
    }
}
