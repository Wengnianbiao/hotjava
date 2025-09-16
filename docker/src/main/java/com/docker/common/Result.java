package com.docker.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -6168755199671271113L;

    private String returnCode; // 状态码，200 表示成功
    private String errorMessage; // 描述信息
    private T data; // 泛型数据

    private Result(String code, String message) {
        this.returnCode = code;
        this.errorMessage = message;
    }

    private Result(String code, String message, T data) {
        this.returnCode = code;
        this.errorMessage = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     */
    public static <T> Result<T> success() {
        return new Result<>("1", "成功");
    }

    public static <T> Result<T> success(T data) {
        return new Result<>("1", "成功", data);
    }

    public static <T> Result<T> success200(T data) {
        return new Result<>("200", "成功", data);
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> failed(String message) {
        return new Result<>("500", message);
    }

    public static <T> Result<T> failed(String code, String message) {
        return new Result<>(String.valueOf(code), message);
    }
}

