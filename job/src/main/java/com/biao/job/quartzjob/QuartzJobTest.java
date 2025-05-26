package com.biao.job.quartzjob;

import com.biao.job.quartzjob.job.BreakfastJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 通过创建一个JobDetail和Trigger，然后通过SchedulerFactory创建一个Scheduler
 * 将作业和触发器添加到调度器中，并启动调度器来执行调度
 * 实现了任务和触发器、调度器的解耦，这也是quartz之所以优秀的核心原因
 */
public class QuartzJobTest {

    public static void main(String[] args) throws SchedulerException {

        // 创建调度器工厂
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        // 获取调度器
        Scheduler scheduler = schedulerFactory.getScheduler();

        // 创建作业:建造者模式
        JobDetail jobDetail = JobBuilder.newJob(BreakfastJob.class)
                .withIdentity("testJob", "default_group")
                .usingJobData("taskId", 1)
                .build();

        // 创建触发器:建造者模式
        // 这里的schedule是怎么根据使用的模式最终去执行呢Job的呢
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("testTrigger", "default_group")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 34 16 * * ?"))
                .build();

        // 将作业和触发器添加到调度器中
        scheduler.scheduleJob(jobDetail, trigger);

        // 启动调度器
        scheduler.start();
    }
}
