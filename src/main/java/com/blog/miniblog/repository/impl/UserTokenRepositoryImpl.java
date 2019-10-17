package com.blog.miniblog.repository.impl;

import com.blog.miniblog.dto.UserToken;
import com.blog.miniblog.repository.UserTokenRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;

public class UserTokenRepositoryImpl implements UserTokenRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    public void upsertUserToken(String email, String token) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        UserToken userToken = mongoTemplate.findOne(query, UserToken.class);

        if (userToken == null) {
            UserToken t = new UserToken();
            t.setToken(token);
            t.setEmail(email);
            Date time = new Date();
            t.setCreateTime(time.getTime());
            t.setUpdateTime(time.getTime());
            mongoTemplate.save(t);
        } else {
            Update update = new Update();
            update.set("token", token);
            update.set("updateTime", new Date().getTime());
            mongoTemplate.upsert(query, update, UserToken.class);
        }
    }
}
