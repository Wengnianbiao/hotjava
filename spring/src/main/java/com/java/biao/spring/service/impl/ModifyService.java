package com.java.biao.spring.service.impl;

import com.java.biao.spring.annotation.GPService;
import com.java.biao.spring.service.IModifyService;

@GPService
public class ModifyService implements IModifyService {
    @Override
    public String add(String name, String addr) throws Exception {
        throw new Exception("抛出异常,测试切面通知是否生效!");
//        return "modifyService add,name=" + name + ",addr=" + addr;
    }

    @Override
    public String remove(Integer id) throws Exception {
        return "modifyService remove,id=" + id;
    }

    @Override
    public String edit(Integer id, String name) throws Exception {
        return "modifyService edit,id=" + id + ",name=" + name;
    }
}
