package com.docker.request;

import lombok.Data;

@Data
public class QueryPatientInfo {

    /**
     * 过滤类型
     */
    private Integer filterType;

    /**
     * 过滤内容
     */
    private String filterContent;
}
