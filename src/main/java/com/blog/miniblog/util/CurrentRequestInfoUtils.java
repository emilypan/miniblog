package com.blog.miniblog.util;

import com.blog.miniblog.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Slf4j
public class CurrentRequestInfoUtils {
    private static final long serialVersionUID = -2550185165626007488L;

    public static void setAttributes(String name, Object value) {
        if ( StringUtils.isEmpty(name) || value == null) {
            log.trace("cannot set attributes for name[{}] : {}", name, value);
            return;
        }

        log.trace("set value by name[{}] to current request attributes : {}", name, value);

        try {
            RequestAttributes reqAttr = RequestContextHolder.currentRequestAttributes();
            reqAttr.setAttribute(name, value, RequestAttributes.SCOPE_REQUEST);
            RequestContextHolder.setRequestAttributes(reqAttr, true);
        } catch (IllegalStateException e) {
            log.trace("no thread bound request context found");
        }
    }

    public static <T> T getAttributes(String name, Class<T> clazz) {
        Object value = null;

        try {
            value = RequestContextHolder.currentRequestAttributes().getAttribute(name, RequestAttributes.SCOPE_REQUEST);
        } catch (IllegalStateException e) {
            log.trace("no thread bound request context found");
        }

        T result = null;
        if (value != null) {
            result = clazz.cast(value);
        }

        log.trace("get value by name[{}] from current request attributes : {}", name, value);

        return result;
    }

    public static void remove() {
        log.trace("remove current request attributes");
        RequestContextHolder.resetRequestAttributes();
    }

    public static User getCurrentUser() {
        return getAttributes(Constants.X_CURRENT_USER, User.class);
    }

    public static String getUsername() {
        return getAttributes(Constants.X_CURRENT_USERNAME, String.class);
    }
}
