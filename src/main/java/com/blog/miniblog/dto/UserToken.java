package com.blog.miniblog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(
        collection = "userToken"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserToken {

    @Id
    private String id;
    private String email;
    private String token;
    @CreatedDate
    private Long createTime;
    @LastModifiedDate
    private Long updateTime;
}