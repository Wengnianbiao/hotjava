package com.java.biao.spring.action;

import com.java.biao.spring.annotation.GPAutowired;
import com.java.biao.spring.annotation.GPController;
import com.java.biao.spring.annotation.GPRequestMapping;
import com.java.biao.spring.annotation.GPRequestParam;
import com.java.biao.spring.mvc.GPModelAndView;
import com.java.biao.spring.service.IQueryService;

import java.util.HashMap;
import java.util.Map;

@GPController
@GPRequestMapping("/")
public class PageAction {

    @GPAutowired
    IQueryService queryService;

    @GPRequestMapping("/first.html")
    public GPModelAndView query(@GPRequestParam("teacher") String teacher) {
        String result = queryService.query(teacher);
        Map<String, Object> model = new HashMap<>();
        model.put("teacher", teacher);
        model.put("data", result);
        model.put("token", "123456");
        return new GPModelAndView("first.html", model);
    }
}
