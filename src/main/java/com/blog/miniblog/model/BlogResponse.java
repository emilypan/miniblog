package com.blog.miniblog.model;

import com.blog.miniblog.dto.Blog;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogResponse {
    private int count; // TODO change to use total number
    // TODO add field for pageNum and pageSize
    private long totalNumber;
    private int pageNum;
    private int pageSize;
    private List<Blog> blogs;
//    private Blog.Author author;
//    private String text;
    public BlogResponse(List<Blog> blogs) {
        this.blogs = new ArrayList<>();
        this.blogs.addAll(blogs);
        this.count = this.blogs.size();
    }
    public BlogResponse(Blog blog) {
        this.blogs = new ArrayList<>();
        if(blog!=null) {
            this.blogs.add(blog);
        }
        this.count = this.blogs.size();
    }
}
