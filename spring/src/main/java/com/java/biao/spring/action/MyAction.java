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

    @GPAutowired("queryServiceImpl")
    IQueryService queryService;

    @GPAutowired("modifyServiceImpl")
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
            // 设置响应的字符编码为 UTF-8
            response.setCharacterEncoding("UTF-8");
            // 设置 Content-Type 头，确保客户端使用 UTF-8 解码
            response.setContentType("application/json;charset=UTF-8"); // 或 "text/plain;charset=UTF-8"
            // 写入响应内容
            response.getWriter().write(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
