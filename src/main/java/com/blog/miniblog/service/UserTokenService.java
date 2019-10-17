package com.blog.miniblog.service;

import org.springframework.stereotype.Service;

@Service
public interface UserTokenService {
    //void saveUserToken(UserToken userToken);
    void upsertUserToken(String email, String token);
}
