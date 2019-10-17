package com.blog.miniblog.repository;

public interface UserTokenRepositoryCustom {
    void upsertUserToken(String email, String token);
 }
