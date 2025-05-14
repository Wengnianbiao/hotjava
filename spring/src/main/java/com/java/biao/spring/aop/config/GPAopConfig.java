package com.java.biao.spring.aop.config;

import lombok.Data;

/**
 * AOP的上下文配置
 */
@Data
public class GPAopConfig {

    private String pointCut;
    private String aspectBefore;
    private String aspectAfter;
    private String aspectClass;
    private String aspectAfterThrow;
    private String aspectAfterThrowingName;
}
