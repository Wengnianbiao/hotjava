package com.biao.job.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Scheduled的底层是依赖ThreadPoolTaskScheduler来执行任务默认的线程大小为1
 * 也就是说在不增加ThreadPoolTaskScheduler的线程数的情况下会存在任务饥饿问题
 * 譬如 在任务 1 执行时，任务 2 不会执行，因为任务 1 的执行时间过长，导致任务 2 被阻塞，直到任务 1 执行完毕。
 * 因此为了解决 任务饥饿问题，需要增加 ThreadPoolTaskScheduler 的线程数。
 * 最佳实践是配置ThreadPoolTaskScheduler，见 SchedulerConfig（线程池相关配置可统一配置或者在具体业务中配置，提高可读性）
 */
@Component
public class BlockingTaskDemo {

    // 任务 1：模拟耗时任务，执行时间为 15 秒
    @Scheduled(fixedRate = 5000) // 每隔 5 秒触发一次
    public void longRunningTask() {
        System.out.println("Long running task started at: " + formatDate(new Date()));
        try {
            Thread.sleep(15000); // 模拟任务执行耗时 15 秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Long running task finished at: " + formatDate(new Date()));
    }

    // 任务 2：快速任务，执行时间很短
    @Scheduled(fixedRate = 5000) // 每隔 5 秒触发一次
    public void quickTask() {
        System.out.println("Quick task executed at: " + formatDate(new Date()));
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return sdf.format(date);
    }
}
