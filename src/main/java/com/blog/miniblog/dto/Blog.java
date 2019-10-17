package com.blog.miniblog.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(
        collection = "blog"
)
//@CompoundIndexes({
//        @CompoundIndex(name = "contact_email", def = "{ 'author.mail': 1 }", unique = true)
//})
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class Blog {

    @Id
    private String id;
    private String title;
    private String content;
    private long size;
    private Author author;
//    private ArrayList<String> imageIds;
    @CreatedDate
    private Long createTime;
    @LastModifiedDate
    private Long updateTime;

    private Boolean isPublic = true;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Author {
        @JsonIgnore
        private String userId;
        private String name;
        private String email;
    }
}