package com.biao.job.quartzjob.mapper;

import com.biao.job.quartzjob.model.dto.BusinessTaskDTO;
import com.biao.job.quartzjob.model.entity.BusinessTaskDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BusinessTaskMapper extends Mapper<BusinessTaskDO> {

    List<BusinessTaskDTO> selectAllTask();

    @Update("update business_task set latest_execute_time = #{latestExecuteTime} where id = #{id}")
    void updateLatestExecuteTimeById(@Param("id") Integer id, @Param("latestExecuteTime") String latestExecuteTime);
}
