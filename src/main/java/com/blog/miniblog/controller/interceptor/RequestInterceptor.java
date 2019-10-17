package com.blog.miniblog.controller.interceptor;

import com.blog.miniblog.common.ResultCode;
import com.blog.miniblog.dto.User;
import com.blog.miniblog.exception.ResourceInvalidException;
import com.blog.miniblog.exception.ResourceNotFoundException;
import com.blog.miniblog.service.JwtTokenService;
import com.blog.miniblog.service.UserService;
import com.blog.miniblog.util.Constants;
import com.blog.miniblog.util.CurrentRequestInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static final String EXECUTION_START_TIME = "execution_start_time";

    @Autowired
    UserService userService;
    @Autowired
    JwtTokenService jwtTokenService;

    @Override
    public boolean preHandle(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Object handler) {
        log.debug("preHandle");
        String token = httpRequest.getHeader(Constants.X_JWT);

        if (StringUtils.isEmpty(token)) {
            String uri = httpRequest.getRequestURI();
            log.debug("request uri {}",uri);
            throw new ResourceInvalidException(ResultCode.JWT_INVALID, "X-JWT not exists.");
        }

        User currentUser = jwtTokenService.parseJwtToken(token);

        log.debug("currentUser email {}",currentUser.getEmail());
        User user = userService.findByEmail(currentUser.getEmail());
        if (user == null || StringUtils.isEmpty(user.getEmail())) {
            throw new ResourceNotFoundException(ResultCode.USER_NOT_FOUND, "User not found, invalid account.");
        }

        CurrentRequestInfoUtils.setAttributes(Constants.X_CURRENT_USER, user);
        CurrentRequestInfoUtils.setAttributes(Constants.X_CURRENT_EMAIL, user.getEmail());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.debug("postHandle");
        CurrentRequestInfoUtils.remove();
    }
}
