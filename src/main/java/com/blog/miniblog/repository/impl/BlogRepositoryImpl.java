package com.blog.miniblog.repository.impl;

import com.blog.miniblog.dto.Blog;
import com.blog.miniblog.repository.BlogRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.Date;
import java.util.List;

@Slf4j
public class BlogRepositoryImpl implements BlogRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Blog> findByAuthorName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("author.name").is(name));
        query.with(new Sort(Sort.Direction.DESC, "createTime"));
        return mongoTemplate.find(query, Blog.class);
    }

    @Override
    public Page<Blog> findByEmail(String email, int pageNum, int pageSize) {
        Pageable pageableRequest = PageRequest.of(pageNum, pageSize);
        Query query = new Query();
        query.addCriteria(Criteria.where("author.email").is(email));
        query.with(new Sort(Sort.Direction.DESC, "createTime"));
        query.with(pageableRequest);
        List<Blog> blogs = mongoTemplate.find(query, Blog.class);

        Page<Blog> blogPage = PageableExecutionUtils.getPage(
                blogs,
                pageableRequest,
                () -> mongoTemplate.count(query, Blog.class));
        return blogPage;
    }

    @Override
    public Blog findByUserAndBlogId(String email, String blogId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("author.email").is(email));
        query.addCriteria(Criteria.where("id").is(blogId));
        return mongoTemplate.findOne(query, Blog.class);
    }

    @Override
    public Page<Blog> findByUserIds(List<String> userIds, int pageNum, int pageSize) {
        Pageable pageableRequest = PageRequest.of(pageNum, pageSize);
        Query query = new Query();
        query.addCriteria(Criteria.where("author.userId").in(userIds));
        query.addCriteria(Criteria.where("isPublic").is(true));
        query.with(new Sort(Sort.Direction.DESC, "createTime"));
        query.with(pageableRequest);
        List<Blog> blogs =  mongoTemplate.find(query, Blog.class);
        Page<Blog> blogPage = PageableExecutionUtils.getPage(
                blogs,
                pageableRequest,
                () -> mongoTemplate.count(query, Blog.class));
        return blogPage;
    }
}
