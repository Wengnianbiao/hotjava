package com.docker.request;

import lombok.Data;

import java.util.List;

@Data
public class LisApplyInfos {

    /**
     * 病人 ID
     */
    private String patientId;

    /**
     * 门诊号
     */
    private String clinicId;

    /**
     * 体检编号
     */
    private String patientCode;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private String age;

    /**
     * 性别
     */
    private String sex;


    private List<Order> orders;


    @Data
    public static class Order {

        /**
         * 检验项目 ID
         */
        private String sourceId;

        /**
         * 检验项目 ID
         */
        private String ordersId;

        /**
         * 标本
         */
        private String sample;

        /**
         * 申请科室 ID
         */
        private String ordersDept;

        /**
         * 执行科室 ID
         */
        private String executeDept;

        /**
         * 申请人员 ID
         */
        private String ordersManId;

        /**
         * 申请时间
         */
        private String ordersDate;

        /**
         * 条码号
         */
        private String barCode;
    }
}
