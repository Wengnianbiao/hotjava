package com.docker.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultJarvis<T> implements Serializable {

    private static final long serialVersionUID = -6168755199671271113L;

    private String code; // 状态码，200 表示成功
    private String message; // 描述信息
    private T data; // 泛型数据

    private ResultJarvis(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private ResultJarvis(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     */
    public static <T> ResultJarvis<T> success() {
        return new ResultJarvis<>("200", "成功");
    }

    public static <T> ResultJarvis<T> success(T data) {
        return new ResultJarvis<>("1", "成功", data);
    }

    public static <T> ResultJarvis<T> success200(T data) {
        return new ResultJarvis<>("200", "成功", data);
    }

    /**
     * 失败返回结果
     */
    public static <T> ResultJarvis<T> failed(String message) {
        return new ResultJarvis<>("500", message);
    }

    public static <T> ResultJarvis<T> failed(String code, String message) {
        return new ResultJarvis<>(String.valueOf(code), message);
    }
}

