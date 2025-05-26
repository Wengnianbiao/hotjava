package com.biao.job.scheduled;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyScheduledTasks {

    // 使用线程池1执行
    @Scheduled(cron = "")
//    @Async("cpuTaskExecutor")  // 指定线程池Bean名称
    public void cpuIntensiveTask() {
        Date start = new Date();
        System.out.println(start.getTime() + ":CPU任务执行线程: " + Thread.currentThread().getName());
    }

    // 使用线程池2执行
    @Scheduled(fixedDelay = 1000)
//    @Async("ioTaskExecutor")
    public void ioIntensiveTask() {
        Date start = new Date();
        System.out.println(start.getTime() + ":IO任务执行线程: " + Thread.currentThread().getName());
        try {
            System.out.println("开始睡眠");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
