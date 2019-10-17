package com.blog.miniblog.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "role")
public class UserRole {
//    @Id
//    private String id;
    private ROLENAME role = ROLENAME.USER;
    private String name;

    public enum ROLENAME {
        ADMIN,
        USER
    }

    private List<UserPermission> userPermissions;
}

