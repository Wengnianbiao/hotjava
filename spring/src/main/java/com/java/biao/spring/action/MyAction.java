package com.java.biao.spring.action;

import com.java.biao.spring.annotation.GPAutowired;
import com.java.biao.spring.annotation.GPController;
import com.java.biao.spring.annotation.GPRequestMapping;
import com.java.biao.spring.annotation.GPRequestParam;
import com.java.biao.spring.mvc.GPModelAndView;
import com.java.biao.spring.service.IModifyService;
import com.java.biao.spring.service.IQueryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@GPController
@GPRequestMapping("/web")
public class MyAction {

    @GPAutowired
    IQueryService queryService;

    @GPAutowired
    IModifyService modifyService;


    @GPRequestMapping("/query.json")
    public GPModelAndView query(HttpServletRequest request, HttpServletResponse response,
                                @GPRequestParam("name") String name) {
        System.out.println("接口访问");
        String result = queryService.query(name);
        return out(response, result);
    }

    @GPRequestMapping("/add*.json")
    public GPModelAndView add(HttpServletRequest request, HttpServletResponse response,
                                @GPRequestParam("name") String name, @GPRequestParam("addr") String addr) throws Exception {
        String result = modifyService.add(name, addr);
        return out(response, result);
    }

    @GPRequestMapping("/remove.json")
    public GPModelAndView remove(HttpServletRequest request, HttpServletResponse response,
                                 @GPRequestParam("id") Integer id) throws Exception {
        String result = modifyService.remove(id);
        return out(response, result);
    }

    @GPRequestMapping("/edit.json")
    public GPModelAndView edit(HttpServletRequest request, HttpServletResponse response,
                               @GPRequestParam("id") Integer id, @GPRequestParam("name") String name) throws Exception {
        String result = modifyService.edit(id, name);
        return out(response, result);
    }

    private GPModelAndView out(HttpServletResponse response, String str) {
        try {
            response.getWriter().write(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
