package com.blog.miniblog.repository;

import com.blog.miniblog.dto.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogRepository extends MongoRepository<Blog, String>, BlogRepositoryCustom {
    //Optional<Blog> findById(String id);
}
