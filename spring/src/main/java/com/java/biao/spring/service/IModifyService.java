package com.java.biao.spring.service;

public interface IModifyService {

    String add(String name, String addr) throws Exception;

    String remove(Integer id) throws Exception;

    String edit(Integer id, String name) throws Exception;
}
