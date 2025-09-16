package com.docker.request;

import lombok.Data;

import java.util.List;

@Data
public class PacsApplyInfos {

    List<PacsApplyInfo> data;


    @Data
    public static class PacsApplyInfo {

        /**
         * 体检系统申请单号
         */
        private String sourceId;

        /**
         * 过滤内容
         */
        private String medTechRequestId;
    }
}
