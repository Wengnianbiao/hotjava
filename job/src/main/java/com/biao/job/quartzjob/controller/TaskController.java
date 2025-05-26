package com.biao.job.quartzjob.controller;

import com.biao.job.quartzjob.common.Result;
import com.biao.job.quartzjob.mapper.BusinessTaskMapper;
import com.biao.job.quartzjob.model.dto.BusinessTaskDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class TaskController {

    private final BusinessTaskMapper businessTaskMapper;

    @GetMapping("list-tasks")
    public Result<List<BusinessTaskDTO>> getAllTasks() {
        List<BusinessTaskDTO> businessTaskDTOS = businessTaskMapper.selectAllTask();
        return Result.success(businessTaskDTOS);
    }
}
