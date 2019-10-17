package com.blog.miniblog.repository.impl;

import com.blog.miniblog.dto.User;
import com.blog.miniblog.repository.UserRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

@Slf4j
public class UserRepositoryImpl implements UserRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public User findByEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        return mongoTemplate.findOne(query, User.class);
    }

    @Override
    public User findByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query, User.class);
    }

    @Override
    public User addFollowings(String userId, List<String> followings) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(userId));

        Update update = new Update();
        update.addToSet("followings").each(followings);

        mongoTemplate.updateFirst(query, update, User.class);
        return mongoTemplate.findOne(query, User.class);
    }

    @Override
    public User removeFollowings(String userId, List<String> followings) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(userId));

        Update update = new Update();
        update.pullAll("followings",followings.toArray());

        mongoTemplate.updateFirst(query, update, User.class);
        return mongoTemplate.findOne(query, User.class);
    }

    @Override
    public List<User> findByUserIds(List<String> userIds) {
        log.debug("findByIds");
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(userIds));
        return mongoTemplate.find(query, User.class);
    }


}