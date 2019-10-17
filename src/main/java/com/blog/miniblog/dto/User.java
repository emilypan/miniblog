package com.blog.miniblog.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Set;

@Data
@Document(
        collection = "user"
)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {

    @Id
    private String id;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    private String email;
    @JsonIgnore
    private String password;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<UserRole> roles;
    //private String encryptedPassword;

    @CreatedDate
    private Long createTime;
    @LastModifiedDate
    private Long updateTime;

    //private Set<String> followers;
    private Set<String> followings;

//    @JsonIgnore
//    private Boolean isBlogPublic = true;

    public User(String email) {
        this.email = email;
    }
}