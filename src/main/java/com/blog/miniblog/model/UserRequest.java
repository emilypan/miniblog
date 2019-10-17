package com.blog.miniblog.model;

import lombok.Data;

@Data
public class UserRequest {
    private String email;
    private String name;
    private String role;
}