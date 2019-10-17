package com.blog.miniblog.repository;

import com.blog.miniblog.dto.Blog;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BlogRepositoryCustom {
    List<Blog> findByAuthorName(String name);
    Page<Blog> findByEmail(String email, int pageNum, int pageSize);
    Blog findByUserAndBlogId(String email,String blogId);
    Page<Blog> findByUserIds(List<String> userIds, int pageNum, int pageSize);
}
