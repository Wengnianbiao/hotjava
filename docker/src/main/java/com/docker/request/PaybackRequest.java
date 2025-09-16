package com.docker.request;

import lombok.Data;

@Data
public class PaybackRequest {
    /**
     * 体检编号
     */
    private String patientCode;


    private String presId;
}

