package com.blog.miniblog.model;

import com.blog.miniblog.dto.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFollowResponse implements Serializable {
    private List<User> users;
    private int count;
    public UserFollowResponse(List<User> users) {
        this.users = new ArrayList<>();
        this.users.addAll(users);
        this.count = this.users.size();
    }
}