package com.blog.miniblog.service;

import com.blog.miniblog.dto.Blog;
import com.blog.miniblog.dto.User;
import com.blog.miniblog.model.BlogRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BlogService {
    List<Blog> getBlogs(int pageNum, int pageSize);
    Page<Blog> getBlogs(String email, int pageNum, int pageSize);
    Blog getBlogById(String email, String blogId);
    List<Blog> searchBlogsByAuthorname(String name, int pageNum, int pageSize);
    Blog createBlog(Blog blog);
    Blog updateBlog(User user, String blogId, BlogRequest request);
    void deleteBlog(String email, String blogId);
    Page<Blog> getFollowBlogs(User user, List<String> userIds, int pageNum, int pageSize);
    Page<Blog> getAllFollowBlogs(User user, int pageNum, int pageSize);
}
