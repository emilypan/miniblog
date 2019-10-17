package com.blog.miniblog.service.impl;

import com.blog.miniblog.common.ResultCode;
import com.blog.miniblog.dto.Blog;
import com.blog.miniblog.dto.User;
import com.blog.miniblog.exception.ResourceNotFoundException;
import com.blog.miniblog.model.BlogRequest;
import com.blog.miniblog.repository.BlogRepository;
import com.blog.miniblog.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    public BlogRepository blogRepository;

    @Override
    public List<Blog> getBlogs(int pageNum, int pageSize) {
        return blogRepository.findAll();
    }

    @Override
    public Page<Blog> getBlogs(String email, int pageNum, int pageSize) {
        return blogRepository.findByEmail(email,pageNum,pageSize);
    }

    @Override
    public Blog getBlogById(String email, String blogId) {
        return blogRepository.findByUserAndBlogId(email,blogId);
    }

    @Override
    public List<Blog> searchBlogsByAuthorname(String name, int pageNum, int pageSize) {
        return blogRepository.findByAuthorName(name);
    }

    @Override
    public Blog createBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public Blog updateBlog(User user, String blogId, BlogRequest request) {
        Blog blog = blogRepository.findByUserAndBlogId(user.getEmail(), blogId);
        if (blog==null) {
            throw new ResourceNotFoundException(ResultCode.RESOURCE_NOT_FOUND, "Blog not exists");
        }
        blog.setUpdateTime(new Date().getTime());
        if( !StringUtils.isEmpty(request.getTitle()) ) {
            blog.setTitle(request.getTitle());
        }
        if(!StringUtils.isEmpty(request.getContent())) {
            blog.setContent(request.getContent());
        }
        blog.setAuthor(new Blog.Author(user.getId(),user.getName(),user.getEmail()));
        if(request.getIsPublic()!=null) {
            blog.setIsPublic(request.getIsPublic());
        }
        return blogRepository.save(blog);
    }

    @Override
    public void deleteBlog(String email, String blogId) {
        Blog b = blogRepository.findByUserAndBlogId(email, blogId);
        if (b==null) {
            throw new ResourceNotFoundException(ResultCode.RESOURCE_NOT_FOUND, "Blog not exists");
        }
        blogRepository.delete(b);
    }

    @Override
    public Page<Blog> getFollowBlogs(User user, List<String> userIds, int pageNum, int pageSize) {
        // check user following list
        if (user.getFollowings()!=null) {
            List<String> originalList = new ArrayList(user.getFollowings());
            List<String> filteredList = userIds.stream()
                    .filter(x -> originalList.contains(x))
                    .filter(x -> !x.equalsIgnoreCase(user.getId()))
                    .collect(Collectors.toList());
            return blogRepository.findByUserIds(filteredList,pageNum,pageSize);
        }
        Pageable pageableRequest = PageRequest.of(pageNum, pageSize);
        Page<Blog> blogPage = PageableExecutionUtils.getPage(
                new ArrayList<Blog>(),
                pageableRequest,
                () -> 0);
        return blogPage;
    }

    @Override
    public Page<Blog> getAllFollowBlogs(User user, int pageNum, int pageSize) {
        if (user.getFollowings()!=null) {
            List<String> followList = new ArrayList(user.getFollowings());
            List<String> filteredList = followList.stream()
                    .filter(x -> !x.equalsIgnoreCase(user.getId()))
                    .collect(Collectors.toList());
            return blogRepository.findByUserIds(filteredList,pageNum,pageSize);
        }
        Pageable pageableRequest = PageRequest.of(pageNum, pageSize);
        Page<Blog> blogPage = PageableExecutionUtils.getPage(
                new ArrayList<Blog>(),
                pageableRequest,
                () -> 0);
        return blogPage;
    }
}
