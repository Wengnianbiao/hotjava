package com.biao.job.quartzjob.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@ApiModel(value = "BusinessTask",description = "业务类定时任务")
@Data
@Entity
@Table(name = "business_task")
public class BusinessTaskDO {

    @ApiModelProperty(value ="主键id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value ="任务bean名称")
    @Column(name = "task_bean")
    private String taskBean;

    @ApiModelProperty(value ="任务描述")
    @Column(name = "task_desc")
    private String taskDesc;

    @ApiModelProperty(value ="cron表达式")
    @Column(name = "cron_expression")
    private String cronExpression;

    @ApiModelProperty(value ="启动状态 0:关闭 1:启动")
    @Column(name = "open_status")
    private Integer openStatus;

    @ApiModelProperty(value ="参数")
    @Column(name = "params")
    private String params;

    @ApiModelProperty(value ="最近一次执行时间")
    @Column(name = "latest_execute_time")
    private String latestExecuteTime;
}
