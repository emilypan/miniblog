package com.blog.miniblog.model;

import lombok.Data;

import java.util.List;

@Data
public class UserFollowRequest {
    private List<String> userIds;
}