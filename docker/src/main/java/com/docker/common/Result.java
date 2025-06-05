package com.docker.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -6168755199671271113L;

    private int code; // 状态码，200 表示成功
    private String message; // 描述信息
    private T data; // 泛型数据

    private Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "成功");
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "成功", data);
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> failed(String message) {
        return new Result<>(500, message);
    }

    public static <T> Result<T> failed(int code, String message) {
        return new Result<>(code, message);
    }
}

