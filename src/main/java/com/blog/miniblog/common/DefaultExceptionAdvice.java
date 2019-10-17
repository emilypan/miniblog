package com.blog.miniblog.common;

import com.blog.miniblog.exception.AuthorizationException;
import com.blog.miniblog.exception.ResourceExistException;
import com.blog.miniblog.exception.ResourceInvalidException;
import com.blog.miniblog.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class DefaultExceptionAdvice extends ResponseEntityExceptionHandler {

    private HttpHeaders generateContentTypeHeaders(HttpServletRequest req) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return responseHeaders;
    }

    @ExceptionHandler(value = AuthorizationException.class)
    public ResponseEntity<RestResponse<String>> handleAuthorizationException(AuthorizationException ex, HttpServletRequest req) {
        RestResponse<String> failResponse = new RestResponse<>(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(failResponse, generateContentTypeHeaders(req), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = ResourceInvalidException.class)
    public ResponseEntity<RestResponse<String>> handleResourceInvalidException(ResourceInvalidException ex, HttpServletRequest req) {
        RestResponse<String> failResponse = new RestResponse<>(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(failResponse, generateContentTypeHeaders(req), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<RestResponse<String>> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest req) {
        RestResponse<String> failResponse = new RestResponse<>(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(failResponse, generateContentTypeHeaders(req), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ResourceExistException.class)
    public ResponseEntity<RestResponse<String>> handleResourceExistException(ResourceExistException ex, HttpServletRequest req) {
        RestResponse<String> failResponse = new RestResponse<>(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(failResponse, generateContentTypeHeaders(req), HttpStatus.CONFLICT);
    }
}