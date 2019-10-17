package com.blog.miniblog.service.impl;

import com.blog.miniblog.common.ResultCode;
import com.blog.miniblog.dto.User;
import com.blog.miniblog.exception.ResourceInvalidException;
import com.blog.miniblog.exception.ResourceNotFoundException;
import com.blog.miniblog.model.UserRequest;
import com.blog.miniblog.repository.UserRepository;
import com.blog.miniblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    public UserRepository userRepository;

    @Override
    public User findByEmail(String email) {
        log.debug("findByEmail {}",email);
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByName(String name) {
        log.debug("findByUsername {}",name);
        return userRepository.findByName(name);
    }

    @Override
    public void saveUser(User user) {
        log.debug("addUser {}",user);
        userRepository.save(user);
    }

    @Override
    public User updateUser(UserRequest request, User user) {
        log.debug("updateUser {}", request);
        if ( !StringUtils.isEmpty(request.getName()) ) {
            user.setName(request.getName());
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(UserRequest request) {
         if( StringUtils.isEmpty(request.getEmail()) ) {
             throw new ResourceNotFoundException(ResultCode.RESOURCE_NOT_FOUND, "User not exists for delete");
         }
         String email = request.getEmail();
         User user = userRepository.findByEmail(email);
         if(user == null) {
             throw new ResourceNotFoundException(ResultCode.USER_NOT_FOUND, "User not exists for delete");
         }
         userRepository.delete(user);
    }

    @Override
    public User followUsers(String userId, List<String> followings) {
        List<User> fUsers = userRepository.findByUserIds(followings);
        if(fUsers==null || fUsers.size()==0) {
            throw new ResourceInvalidException(ResultCode.RESOURCE_INVALID_PARAM, "Please provide valid followings");
        }
        List<String> validFollowings = new ArrayList<>();
        for(User u: fUsers) {
            validFollowings.add(u.getId());
        }
        return userRepository.addFollowings(userId, validFollowings);
    }

    @Override
    public User unFollowUsers(String userId, List<String> followings) {
        return userRepository.removeFollowings(userId, followings);
    }

    @Override
    public List<User> getAllFollowUsers(User user) {
        if(user.getFollowings()!=null && user.getFollowings().size()>0) {
            List<String> userList = new ArrayList(user.getFollowings());
            return userRepository.findByUserIds(userList);
        }
        return new ArrayList<User>();
    }
}
