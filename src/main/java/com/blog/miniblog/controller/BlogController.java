package com.blog.miniblog.controller;

//import blog.model.AccessTokenJWT;

import com.blog.miniblog.common.ResultCode;
import com.blog.miniblog.dto.Blog;
import com.blog.miniblog.dto.User;
import com.blog.miniblog.exception.ResourceInvalidException;
import com.blog.miniblog.exception.ResourceNotFoundException;
import com.blog.miniblog.model.BlogRequest;
import com.blog.miniblog.model.BlogResponse;
import com.blog.miniblog.model.UserFollowRequest;
import com.blog.miniblog.service.BlogService;
import com.blog.miniblog.util.Constants;
import com.blog.miniblog.util.CurrentRequestInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Slf4j
@RequestMapping("/v1/blog")
public class BlogController {

    @Autowired
    BlogService blogService;

    @PostMapping("")
    public ResponseEntity<Blog> postBlog(@RequestBody BlogRequest request) {
        log.debug("postBlog {}", request);
        // current user
        User user = CurrentRequestInfoUtils.getCurrentUser();
        if(user == null) {
            log.error("cannot find current user information");
            throw new ResourceNotFoundException(ResultCode.USER_NOT_FOUND, "Cannot find current user information");
        }

        if(StringUtils.isEmpty(request.getTitle()) || StringUtils.isEmpty(request.getContent())) {
            log.error("invalid blog for post");
            throw new ResourceNotFoundException(ResultCode.RESOURCE_INVALID_PARAM, "Empty title and content are not allowed");
        }

        Blog blog = new Blog();
        blog.setCreateTime(new Date().getTime());
        blog.setAuthor(new Blog.Author(user.getId(),user.getName(),user.getEmail()));
        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        if(request.getIsPublic()!=null) {
            blog.setIsPublic(request.getIsPublic());
        }

        blog = blogService.createBlog(blog);

        return new ResponseEntity<>(blog, HttpStatus.CREATED);
    }

    @GetMapping("")
    public BlogResponse getBlogs(
            @RequestParam(value = "pageNum", required = false, defaultValue = Constants.PAGE_DEFAULT_START) int pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = Constants.PAGE_DEFAULT_LIMIT) int pageSize
    ) {
        log.debug("getBlogs");
        // current user
        User user = CurrentRequestInfoUtils.getCurrentUser();
        if(user == null) {
            log.error("cannot find current user information");
            throw new ResourceNotFoundException(ResultCode.USER_NOT_FOUND, "Cannot find current user information");
        }
        if (pageNum < 0 || pageSize <= 0) {
            throw new ResourceInvalidException(ResultCode.RESOURCE_INVALID_PARAM, "pageNum/pageSize value is invalid");
        }

        Page<Blog> blogs = blogService.getBlogs(user.getEmail(),pageNum, pageSize);
        BlogResponse response = new BlogResponse(blogs.getContent());
        response.setPageNum(pageNum);
        response.setPageSize(pageSize);
        response.setTotalNumber(blogs.getTotalElements());
        return response;
    }

    @GetMapping("/{id}")
    public BlogResponse getBlog(@PathVariable String id) {
        log.debug("getBlog blogId {}", id);
        // current user
        User user = CurrentRequestInfoUtils.getCurrentUser();
        if(user == null) {
            log.error("cannot find current user information");
            throw new ResourceNotFoundException(ResultCode.USER_NOT_FOUND, "Cannot find current user information");
        }

        Blog blog = blogService.getBlogById(user.getEmail(),id);
        return new BlogResponse(blog);
    }

    @PutMapping("/{id}")
    public BlogResponse updateBlog(@PathVariable String id, @RequestBody BlogRequest request) {
        log.debug("updateBlog blogId {}", id);
        // current user
        User user = CurrentRequestInfoUtils.getCurrentUser();
        if(user == null) {
            log.error("cannot find current user information");
            throw new ResourceNotFoundException(ResultCode.USER_NOT_FOUND, "Cannot find current user information");
        }

        Blog blog = blogService.updateBlog(user, id, request);
        return new BlogResponse(blog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBlog(@PathVariable String id) {
        log.debug("deleteBlog blogId {}", id);
        // current user
        User user = CurrentRequestInfoUtils.getCurrentUser();
        if(user == null) {
            log.error("cannot find current user information");
            throw new ResourceNotFoundException(ResultCode.USER_NOT_FOUND, "Cannot find current user information");
        }

        blogService.deleteBlog(user.getEmail(),id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/follow")
    public BlogResponse getFollowBlogs(
            @RequestBody UserFollowRequest request,
            @RequestParam(value = "pageNum", required = false, defaultValue = Constants.PAGE_DEFAULT_START) int pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = Constants.PAGE_DEFAULT_LIMIT) int pageSize
    ) {
        log.debug("getFollowBlogs {}", request);
        // current user
        User user = CurrentRequestInfoUtils.getCurrentUser();
        if(user == null) {
            log.error("cannot find current user information");
            throw new ResourceNotFoundException(ResultCode.USER_NOT_FOUND, "Cannot find current user information");
        }
        if (pageNum < 0 || pageSize <= 0) {
            throw new ResourceInvalidException(ResultCode.RESOURCE_INVALID_PARAM, "pageNum/pageSize value is invalid");
        }

        Page<Blog> blogs = blogService.getFollowBlogs(user,request.getUserIds(),pageNum,pageSize);
        BlogResponse response = new BlogResponse(blogs.getContent());
        response.setPageNum(pageNum);
        response.setPageSize(pageSize);
        response.setTotalNumber(blogs.getTotalElements());
        return response;
    }

    @GetMapping("/follow")
    public BlogResponse getFollowBlogs(
            @RequestParam(value = "pageNum", required = false, defaultValue = Constants.PAGE_DEFAULT_START) int pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = Constants.PAGE_DEFAULT_LIMIT) int pageSize
    ) {
        log.debug("getFollowBlogs");
        // current user
        User user = CurrentRequestInfoUtils.getCurrentUser();
        if(user == null) {
            log.error("cannot find current user information");
            throw new ResourceNotFoundException(ResultCode.USER_NOT_FOUND, "Cannot find current user information");
        }
        if (pageNum < 0 || pageSize <= 0) {
            throw new ResourceInvalidException(ResultCode.RESOURCE_INVALID_PARAM, "pageNum/pageSize value is invalid");
        }

        Page<Blog> blogs = blogService.getAllFollowBlogs(user,pageNum,pageSize);
        BlogResponse response = new BlogResponse(blogs.getContent());
        response.setPageNum(pageNum);
        response.setPageSize(pageSize);
        response.setTotalNumber(blogs.getTotalElements());
        return response;
    }
}