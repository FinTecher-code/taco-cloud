package com.blog.dto;

import lombok.Data;

/**
 * 统一 API 返回格式
 */
@Data
public class ApiResult<T> {
    private int code;
    private String message;
    private T data;

    private ApiResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(200, "OK", data);
    }

    public static <T> ApiResult<T> success() {
        return new ApiResult<>(200, "OK", null);
    }

    public static <T> ApiResult<T> error(int code, String message) {
        return new ApiResult<>(code, message, null);
    }

    public static <T> ApiResult<T> error(String message) {
        return new ApiResult<>(400, message, null);
    }
}
