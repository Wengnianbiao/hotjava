package com.docker.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.docker.common.Result;
import com.docker.common.ResultJarvis;
import com.docker.request.FeeItemStatePushDTO;
import com.docker.request.LisApplyInfos;
import com.docker.request.PatientInfo;
import com.docker.request.PatientInfoHis;
import com.docker.request.PaybackRequest;
import com.docker.request.QueryPatientInfo;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
@RestController
public class HttpController {

    @PostMapping(value = "create-archive")
    public Result<Object> createArchive(@RequestBody PatientInfo request) {
        log.info("调用接口成功，参数为:{}", JSONObject.toJSONString(request));
        Map<String, String> map = new HashMap<>();
        map.put("patientId", "123445");
        map.put("clinicId", "hl123244");

        return Result.success(map);
    }

    @PostMapping(value = "payback")
    public ResultJarvis<Object> payback(@RequestBody PaybackRequest request) {
        log.info("调用接口成功，参数为:{}", JSONObject.toJSONString(request));

        return ResultJarvis.success200(1);
    }

    @PostMapping(value = "get-archive")
    public Result<Object> getArchive(@RequestBody QueryPatientInfo request) {
        log.info("调用接口成功，参数为:{}", JSONObject.toJSONString(request));
        // 构建病人信息Map
        List<PatientInfoHis> patientInfoList = new ArrayList<>();
        PatientInfoHis patientInfo = new PatientInfoHis();
        patientInfo.setPatientId("123445");
        patientInfo.setClinicId("hl123244");
        patientInfo.setPatientCode("102568390903432");
        patientInfo.setName("张三");
        patientInfo.setSex("男");
        patientInfo.setAge(25);
        patientInfo.setBirthDate("1997-10-19");
        patientInfoList.add(patientInfo);

        return Result.success(patientInfoList);
    }

    @PostMapping(value = "sync-lis-apply")
    public Result<Object> syncLisApply(@RequestBody LisApplyInfos request) {
        log.info("调用接口成功，参数为:{}", JSONObject.toJSONString(request));
        // 构建返回数据
        List<Map<String, String>> dataList = new ArrayList<>();

        List<LisApplyInfos.Order> orders = request.getOrders();
        orders.forEach(order -> {
            Map<String, String> dataItem = new HashMap<>();
            dataItem.put("medTechRequestId", order.getSourceId());
            dataList.add(dataItem);
        });

        return Result.success(dataList);
    }

    @PostMapping(value = "sync-pacs-apply")
    public Result<Object> syncPacsApply(@RequestBody LisApplyInfos request) {
        log.info("调用接口成功，参数为:{}", JSONObject.toJSONString(request));
        // 构建返回数据
        List<Map<String, String>> dataList = new ArrayList<>();

        List<LisApplyInfos.Order> orders = request.getOrders();
        orders.forEach(order -> {
            Map<String, String> dataItem = new HashMap<>();
            dataItem.put("medTechRequestId", order.getSourceId());
            dataList.add(dataItem);
        });

        return Result.success(dataList);
    }

    @ApiOperation(value = "推送收费项目的状态",notes = "接受接口服务推送项目的状态")
    @PostMapping("/receiveState")
    public ResultJarvis<Object> receiveItemState(@RequestBody List<FeeItemStatePushDTO> feeItemStatePushList){
        log.info("ItemResultController.receiveItemState：{}", JSON.toJSONString(feeItemStatePushList));



        return ResultJarvis.success();
    }
}
