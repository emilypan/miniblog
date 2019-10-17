package com.blog.miniblog.service.impl;

import com.blog.miniblog.repository.UserTokenRepository;
import com.blog.miniblog.service.UserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserTokenServiceImpl implements UserTokenService {

    @Autowired
    public UserTokenRepository userTokenRepository;

//    @Override
//    public void saveUserToken(UserToken userToken) {
//        userTokenRepository.save(userToken);
//    }

    @Override
    public void upsertUserToken(String email, String token) {
        userTokenRepository.upsertUserToken(email, token);
    }
}