package com.biao.job.quartzjob;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * Job增强抽象类，拓展
 */
@Slf4j
@DisallowConcurrentExecution
public abstract class AbstractEnhanceJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // todo
        doService(context);
    }

    public abstract void doService(JobExecutionContext context) throws JobExecutionException;
}
