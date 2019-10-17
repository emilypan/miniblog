package com.blog.miniblog.exception;

import com.blog.miniblog.common.ResultCode;
import lombok.Data;

@Data
public class ResourceExistException extends RuntimeException {
    ResultCode errorCode;
    public ResourceExistException() {
        super();
    }
    public ResourceExistException(String message) {
        super(message);
    }
    public ResourceExistException(ResultCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
