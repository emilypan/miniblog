package com.blog.miniblog.repository;

import com.blog.miniblog.dto.User;

import java.util.List;

public interface UserRepositoryCustom {
    User findByEmail(String email);
    User findByName(String name);
    User addFollowings(String userId, List<String> followings);
    User removeFollowings(String userId, List<String> followings);
    List<User> findByUserIds(List<String> userIds);
}
