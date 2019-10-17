package com.blog.miniblog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(
        collection = "permission"
)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPermission implements Serializable {
    @Id
    private String permissionId;
    private String permissionName;
}
