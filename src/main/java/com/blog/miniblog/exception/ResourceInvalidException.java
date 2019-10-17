package com.blog.miniblog.exception;

import com.blog.miniblog.common.ResultCode;
import lombok.Data;

@Data
public class ResourceInvalidException extends RuntimeException {
    ResultCode errorCode;
    public ResourceInvalidException() {
        super();
    }
    public ResourceInvalidException(String message) {
        super(message);
    }
    public ResourceInvalidException(ResultCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
