package com.blog.miniblog.util;

public class Constants {
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";
    public static final String SECRET = "secret";

    public static final String PAGE_DEFAULT_START = "0";
    public static final String PAGE_DEFAULT_LIMIT = "5";

    public static final String X_JWT = "X-JWT";
    public static final String X_CURRENT_USER = "X-CURRENT-USER";
    public static final String X_CURRENT_EMAIL = "X-CURRENT-EMAIL";
    public static final String X_CURRENT_USERNAME = "X-CURRENT-USERNAME";
    public static final String USER_REGISTRATION_API = "/user/signup";
    public static final String USER_AUTHENTICATE_API = "/user/authenticate";
}

