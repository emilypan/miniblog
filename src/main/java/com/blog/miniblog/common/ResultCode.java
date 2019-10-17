package com.blog.miniblog.common;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(
        shape = JsonFormat.Shape.OBJECT
)
public enum ResultCode {
    SUCCESS(2000000, "SUCCESS"),
    RESOURCE_INVALID_PARAM(4000000, "RESOURCE_INVALID_PARAM"),
    RESOURCE_NOT_FOUND(4040000, "RESOURCE_NOT_FOUND"),
    JWT_EXPIRED(4010007, "JWT_EXPIRED"),
    JWT_UNSUPPORTED(4010008, "JWT_UNSUPPORTED"),
    JWT_INVALID(4010010, "JWT_INVALID"),
    USER_NOT_FOUND(4040100,"USER_NOT_FOUND"),
    USER_ALREADY_EXISTS(4090001, "USER_ALREADY_EXISTS");

    private int code;
    private String message;

    private ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
