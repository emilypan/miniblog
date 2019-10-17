package com.blog.miniblog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class BlogRequest {
    private String blogId;
    private String title;
    private String content;
    private Boolean isPublic;

    @JsonIgnore
    public boolean isSearchByBlogId() {
        return !StringUtils.isEmpty(this.getBlogId());
    }
}
