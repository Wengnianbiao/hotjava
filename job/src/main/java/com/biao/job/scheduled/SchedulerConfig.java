package com.biao.job.scheduled;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class SchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5); // 设置线程池大小
        scheduler.setThreadNamePrefix("ScheduledTask-");
        return scheduler;
    }

    @Bean("cpuTaskExecutor")
    public Executor cpuTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 获取当前机器的CPU核心数
        int cpuCoreCount = Runtime.getRuntime().availableProcessors();
        // 核心线程数，对于CPU密集型任务，一般设置为CPU核心数 + 1，多一个线程可在有线程阻塞等情况时保证任务继续推进
        executor.setCorePoolSize(cpuCoreCount + 1);
        // 最大线程数，这里设置为CPU核心数的2倍 + 2，在任务较多且系统资源允许时可适当扩展线程数量
        executor.setMaxPoolSize((cpuCoreCount * 2) + 2);
        // 队列容量，这里设置为100，可根据实际任务量和系统负载调整，用于暂存等待执行的任务
        executor.setQueueCapacity(100);
        // 线程空闲时间，设置为60秒，即空闲线程等待60秒后会被销毁，直到线程数量等于核心线程数
        executor.setKeepAliveSeconds(60);
        // 线程名称前缀，方便在日志等场景中识别该线程池的线程
        executor.setThreadNamePrefix("cpuTaskExecutor-");
        // 拒绝策略，这里采用CallerRunsPolicy，当线程池和队列都满时，由调用线程来执行任务，可防止任务被丢弃
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化线程池
        executor.initialize();
        return executor;
    }

    @Bean("ioTaskExecutor")
    public Executor ioTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 获取当前机器的CPU核心数
        int cpuCoreCount = Runtime.getRuntime().availableProcessors();
        // 核心线程数，对于CPU密集型任务，一般设置为CPU核心数 + 1，多一个线程可在有线程阻塞等情况时保证任务继续推进
        executor.setCorePoolSize(cpuCoreCount + 1);
        // 最大线程数，这里设置为CPU核心数的2倍 + 2，在任务较多且系统资源允许时可适当扩展线程数量
        executor.setMaxPoolSize((cpuCoreCount * 2) + 2);
        // 队列容量，这里设置为100，可根据实际任务量和系统负载调整，用于暂存等待执行的任务
        executor.setQueueCapacity(100);
        // 线程空闲时间，设置为60秒，即空闲线程等待60秒后会被销毁，直到线程数量等于核心线程数
        executor.setKeepAliveSeconds(60);
        // 线程名称前缀，方便在日志等场景中识别该线程池的线程
        executor.setThreadNamePrefix("cpuTaskExecutor-");
        // 拒绝策略，这里采用CallerRunsPolicy，当线程池和队列都满时，由调用线程来执行任务，可防止任务被丢弃
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化线程池
        executor.initialize();
        return executor;
    }
}
