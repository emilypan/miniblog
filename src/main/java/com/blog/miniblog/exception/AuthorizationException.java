package com.blog.miniblog.exception;

import com.blog.miniblog.common.ResultCode;
import lombok.Data;

@Data
public class AuthorizationException extends RuntimeException {
    ResultCode errorCode;
    public AuthorizationException() {
        super();
    }
    public AuthorizationException(String message) {
        super(message);
    }
    public AuthorizationException(ResultCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
