package com.blog.miniblog.repository;

import com.blog.miniblog.dto.UserToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserTokenRepository extends MongoRepository<UserToken, String>, UserTokenRepositoryCustom{
    //UserToken saveUserToken(UserToken userToken);
}
