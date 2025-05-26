package com.biao.job;

import com.biao.job.quartzjob.event.MyApplicationEvent;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.biao.job.quartzjob")
@MapperScan("com.biao.job.quartzjob.mapper")
public class JobApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(JobApplication.class, args);
        MyApplicationEvent demoEvent = new MyApplicationEvent("", "world");
        run.publishEvent(demoEvent);
    }
}
