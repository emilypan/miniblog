package com.blog.miniblog.repository;

import com.blog.miniblog.dto.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom{
}
