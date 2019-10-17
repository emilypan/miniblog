package com.blog.miniblog.model;

import com.blog.miniblog.dto.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private String id;
    private String email;
    private String name;
    private String token;
    private Long createTime;
    private Long updateTime;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        if(!StringUtils.isEmpty(user.getName())) {
            this.name = user.getName();
        }
        if(user.getCreateTime()!=null) {
            this.createTime = user.getCreateTime();
        }
        if(user.getUpdateTime()!=null) {
            this.updateTime = user.getUpdateTime();
        }
    }
}
