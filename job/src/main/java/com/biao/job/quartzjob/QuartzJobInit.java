package com.biao.job.quartzjob;

import com.biao.job.quartzjob.mapper.BusinessTaskMapper;
import com.biao.job.quartzjob.model.dto.BusinessTaskDTO;
import com.biao.job.quartzjob.model.entity.BusinessTaskDO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时任务初始化
 * 容器启动完成后执行
 */
@Component
@Slf4j
@AllArgsConstructor
public class QuartzJobInit implements ApplicationListener<ApplicationReadyEvent>, ApplicationContextAware {

    private final BusinessTaskMapper businessTaskMapper;

    private final QuartzJobHelper quartzJobHelper;

    private ApplicationContext applicationContext;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        businessTaskInit();
    }

    private void businessTaskInit() {
        List<BusinessTaskDTO> businessTasks = businessTaskMapper.selectAllTask();
        log.info("定时任务列表:{}", businessTasks);
        if(CollectionUtils.isEmpty(businessTasks)){
            return;
        }
        // 目前就只支持Cron表达式的任务，后期可重构
        businessTasks.forEach(businessTask -> {
            // 根据DB中任务的Bean名称获取对应的任务对象
            String taskBeanName = businessTask.getTaskBean();
            AbstractEnhanceJob businessJob = applicationContext.getBean(taskBeanName, AbstractEnhanceJob.class);
            try {
                Map<String, Object> map = new HashMap<>();
                map.put("taskId", businessTask.getId());
                quartzJobHelper.saveJobCron(taskBeanName, businessJob.getClass(), businessTask.getCronExpression(), map);
            } catch (Exception e) {
                log.error("定时任务初始化失败", e);
            }
        });
        log.info("定时任务初始化完成");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
