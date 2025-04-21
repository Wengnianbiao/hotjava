package com.java.biao.spring.demo.service.impl;

import com.java.biao.spring.annotation.GPService;
import com.java.biao.spring.demo.service.IDemoService;

@GPService
public class DemoService implements IDemoService {

    @Override
    public String get(String name) {
        return "My name is " + name;
    }
}
