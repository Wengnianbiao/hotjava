package com.java.biao.spring.mvc;

import com.java.biao.spring.annotation.GPRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GPHandlerAdapter {

    public boolean supports(Object handler) {
        return (handler instanceof GPHandlerMapping);
    }

    public GPModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        GPHandlerMapping handlerMapping = (GPHandlerMapping) handler;
        // 每个方法有参数列表
        Map<String, Integer> paramIndexMapping = new HashMap<>();

        // 提取方法中加了注解的参数
        // 把方法上的注解拿到，得到的是一个二维数组
        // 因为一个参数可以有多个注解，而一个方法又有多个参数
        // 一维是参数，二维是参数对应的注解
        Annotation[][] parameterAnnotations = handlerMapping.getMethod().getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof GPRequestParam) {
                    String paramName = ((GPRequestParam) annotation).value();
                    if (!paramName.trim().isEmpty()) {
                        // 这个参数注解所在的下标索引
                        paramIndexMapping.put(paramName, i);
                    }
                }
            }
        }

        // 提取方法中参数类型
        Class<?>[] parameterTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (parameterType == HttpServletRequest.class ||
                    parameterType == HttpServletResponse.class) {
                paramIndexMapping.put(parameterType.getName(), i);
            }
        }

        // 获得方法的形参列表
        Map<String, String[]> params = request.getParameterMap();

        // 构造实参列表,根据req中的参数去检索在方法中的位置,并在value的map中设置对应的值
        Object[] paramValues = new Object[parameterTypes.length];

        for (Map.Entry<String, String[]> param : params.entrySet()) {
            String value = Arrays.toString(param.getValue())
                    .replaceAll("\\[|\\]", "")
                    .replaceAll("\\s", "");
            if (!paramIndexMapping.containsKey(param.getKey())) {
                continue;
            }
            int index = paramIndexMapping.get(param.getKey());
            // 页面上传递过来的参数肯定是String类型
            paramValues[index] = caseStringValue(value, parameterTypes[index]);
        }

        // 设置请求和响应
        if (paramIndexMapping.containsKey(HttpServletRequest.class.getName())) {
            int reqIndex = paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = request;
        }

        if (paramIndexMapping.containsKey(HttpServletResponse.class.getName())) {
            int respIndex = paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = response;
        }

        // 反射调用方法进行处理
        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);
        if (result == null || result instanceof Void) {
            return null;
        }

        boolean isModelAndView = handlerMapping.getMethod().getReturnType() == GPModelAndView.class;
        if(isModelAndView){
            return (GPModelAndView) result;
        }
        return null;

    }

    private Object caseStringValue(String value, Class<?> parameterType) {
        if (String.class == parameterType) {
            return value;
        } else if (Integer.class == parameterType) {
            return Integer.valueOf(value);
        } else if (Double.class == parameterType) {
            return Double.valueOf(value);
        } else {
            return null;
        }
    }
}
