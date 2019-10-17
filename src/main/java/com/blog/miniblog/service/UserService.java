package com.blog.miniblog.service;

import com.blog.miniblog.dto.User;
import com.blog.miniblog.model.UserRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User findByEmail(String email);
    User findByName(String name);
    void saveUser(User user);
    User updateUser(UserRequest request, User user);
    void deleteUser(UserRequest request);
    User followUsers(String userId, List<String> followings);
    User unFollowUsers(String userId, List<String> followings);
    List<User> getAllFollowUsers(User user);
}
