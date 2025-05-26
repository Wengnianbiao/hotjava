package com.biao.job.quartzjob.job;

import com.biao.job.quartzjob.AbstractEnhanceJob;
import com.biao.job.quartzjob.mapper.BusinessTaskMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *  吃早餐定时任务
 */
@Slf4j
@Component
public class BreakfastJob extends AbstractEnhanceJob {

    @Autowired
    private BusinessTaskMapper businessTaskMapper;

    @Override
    public void doService(JobExecutionContext context) throws JobExecutionException {
        Object taskId = context.getJobDetail().getJobDataMap().get("taskId");
        log.info("任务开始执行，任务Bean名称:{}，任务名称:{}.", this.getClass().getSimpleName(),
                taskId);
        log.info("要按时吃早餐！！！");
        businessTaskMapper.updateLatestExecuteTimeById((Integer) taskId, getCurrentTime());
    }

    private String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}
