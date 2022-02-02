package com.jio.signon.controller;

import com.jio.signon.common.ApiResponse;
import com.jio.signon.common.AppException;
import com.jio.signon.db.model.User;
import com.jio.signon.dto.request.user.CreateUserDto;
import com.jio.signon.services.*;

import org.springframework.http.MediaType;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
public class UserController {


    private final UserSecurityService userSecurityService;
    private final UserService userService;
    
    public UserController(UserSecurityService userSercuityService, UserService userService) {
    	this.userSecurityService = userSercuityService;
    	this.userService = userService;
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ApiResponse<User> createUser(@RequestBody @Valid CreateUserDto createUserDto) throws AppException{
        return new ApiResponse.Success<>(userSecurityService.createUser(createUserDto));
    }
}
