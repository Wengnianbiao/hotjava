package com.java.biao.spring.mvc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GPHandlerMapping {

    private Object controller;

    private Method method;

    private Pattern pattern;

}
