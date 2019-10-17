package com.blog.miniblog.controller;

//import blog.model.AccessTokenJWT;

import com.blog.miniblog.common.ResultCode;
import com.blog.miniblog.dto.UserRole;
import com.blog.miniblog.dto.User;
import com.blog.miniblog.exception.ResourceExistException;
import com.blog.miniblog.exception.ResourceInvalidException;
import com.blog.miniblog.exception.ResourceNotFoundException;
import com.blog.miniblog.model.*;
import com.blog.miniblog.service.JwtTokenService;
import com.blog.miniblog.service.UserService;
import com.blog.miniblog.service.UserTokenService;
import com.blog.miniblog.util.CurrentRequestInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

//import org.springframework.security.authentication.AuthenticationManager;

@RestController
@Slf4j
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    public UserService userService;
    @Autowired
    UserTokenService userTokenService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    JwtTokenService jwtTokenService;

    @PostMapping("/signup")
    public ResponseEntity createNewUser(@RequestBody LoginRequest request) {
        log.debug("createNewUser {}", request);
        if ( StringUtils.isEmpty(request.getEmail()) || StringUtils.isEmpty(request.getPassword()) ) {
            throw new ResourceInvalidException(ResultCode.RESOURCE_INVALID_PARAM, "Please fill in username and password");
        }
    
        User userExists = userService.findByEmail(request.getEmail());
        if ( userExists != null ) {
            throw new ResourceExistException(ResultCode.USER_ALREADY_EXISTS, "User is existing");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        if ( !StringUtils.isEmpty(request.getName()) ) {
            user.setName(request.getName());
        }
        UserRole userRole = new UserRole();
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        user.setCreateTime(new Date().getTime());
        userService.saveUser(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //authenticate
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest request) throws Exception {
        log.debug("createAuthenticationToken {}", request);

        if ( StringUtils.isEmpty(request.getEmail()) || StringUtils.isEmpty(request.getPassword()) ) {
            throw new ResourceInvalidException(ResultCode.RESOURCE_INVALID_PARAM, "Please fill in username and password");
        }

        User user = userService.findByEmail(request.getEmail());
        if ( user == null ) {
            throw new ResourceNotFoundException(ResultCode.USER_NOT_FOUND, "User not found.");
        }
        // match password
        if ( !bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword()) ) {
            throw new ResourceNotFoundException(ResultCode.RESOURCE_INVALID_PARAM, "Authentication Failed. Username or Password not valid.");
        }
        // create token
        String token = jwtTokenService.generateToken(request.getEmail(), user.getRoles());
        // save token
        userTokenService.upsertUserToken(request.getEmail(), token);

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @GetMapping("")
    public ResponseEntity<UserResponse> getUser() {
        log.debug("getUser");
        User user = CurrentRequestInfoUtils.getCurrentUser();
        if(user == null) {
            log.error("cannot find current user information");
            throw new ResourceNotFoundException(ResultCode.USER_NOT_FOUND, "Cannot find current user information");
        }
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PostMapping("")
    public ResponseEntity<UserResponse> getGivenUser(@RequestBody UserRequest request) throws Exception {
        log.debug("getGivenUser, get a user");

        User user = CurrentRequestInfoUtils.getCurrentUser();
        if(user == null) {
            log.error("cannot find current user information");
            throw new ResourceNotFoundException(ResultCode.USER_NOT_FOUND, "Cannot find current user information");
        }

        if ( StringUtils.isEmpty(request.getEmail())) {
            throw new ResourceInvalidException(ResultCode.RESOURCE_INVALID_PARAM, "Please provide user email for query");
        }

        User queryUser = userService.findByEmail(request.getEmail());
        if(queryUser == null) {
            log.error("cannot find given user information");
            throw new ResourceNotFoundException(ResultCode.USER_NOT_FOUND, "Cannot find given user information");
        }

        return ResponseEntity.ok(new UserResponse(queryUser));
    }

    @PutMapping("")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest request) throws Exception {
        log.debug("updateUser, update current user");

        User user = CurrentRequestInfoUtils.getCurrentUser();
        if(user == null) {
            log.error("cannot find current user information");
            throw new ResourceNotFoundException(ResultCode.USER_NOT_FOUND, "Cannot find current user information");
        }

        user = userService.updateUser(request, user);

        return ResponseEntity.ok(new UserResponse(user));
    }

    @DeleteMapping("")
    public ResponseEntity deleteUser(@RequestBody UserRequest request) throws Exception {
        log.debug("deleteUser, delete a user");

        User user = CurrentRequestInfoUtils.getCurrentUser();
        if(user == null) {
            log.error("cannot find current user information");
            throw new ResourceNotFoundException(ResultCode.USER_NOT_FOUND, "Cannot find current user information");
        }

        userService.deleteUser(request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/follow")
    public ResponseEntity<UserFollowResponse> getFollows() {
        log.debug("getFollows");
        User user = CurrentRequestInfoUtils.getCurrentUser();
        if(user == null) {
            log.error("cannot find user information");
            throw new ResourceNotFoundException(ResultCode.USER_NOT_FOUND, "Cannot find current user information");
        }

        List<User> users = userService.getAllFollowUsers(user);
        return new ResponseEntity<>(new UserFollowResponse(users), HttpStatus.OK);
    }

    @PostMapping("/follow")
    public ResponseEntity<User> follow(@RequestBody UserFollowRequest request) {
        log.debug("follow {}", request);
        User user = CurrentRequestInfoUtils.getCurrentUser();
        if(user == null) {
            log.error("cannot find user information");
            throw new ResourceNotFoundException(ResultCode.USER_NOT_FOUND, "Cannot find current user information");
        }

        User updatedUser = userService.followUsers(user.getId(), request.getUserIds());
        return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
    }

    @PostMapping("/unfollow")
    public ResponseEntity<User> unfollow(@RequestBody UserFollowRequest request) {
        log.debug("follow {}", request);
        User user = CurrentRequestInfoUtils.getCurrentUser();
        if(user == null) {
            log.error("cannot find user information");
            throw new ResourceNotFoundException(ResultCode.USER_NOT_FOUND, "Cannot find current user information");
        }
        User updatedUser = userService.unFollowUsers(user.getId(), request.getUserIds());
        return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
    }
}