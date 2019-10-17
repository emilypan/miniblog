package com.blog.miniblog.model;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class LoginRequest {
    @Size(min = 1, max = 200)
    private String email;
    @Size(min = 1, max = 200)
    private String password;
    private String name;
    private String role;
}