package com.java.biao.spring.service.impl;

import com.java.biao.spring.annotation.GPService;
import com.java.biao.spring.service.IQueryService;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@GPService
@Slf4j
public class QueryServiceImpl implements IQueryService {

    @Override
    public String query(String name) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        String json = "{name:\"" + name + "\",time:\"" + time + "\"}";
        log.info("业务方法中的打印信息:{}", json);
        return json;
    }
}
