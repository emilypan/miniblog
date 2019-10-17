package com.blog.miniblog.exception;

import com.blog.miniblog.common.ResultCode;
import lombok.Data;

@Data
public class ResourceNotFoundException extends Error {
    ResultCode errorCode;
    public ResourceNotFoundException() {
        super();
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException(ResultCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
