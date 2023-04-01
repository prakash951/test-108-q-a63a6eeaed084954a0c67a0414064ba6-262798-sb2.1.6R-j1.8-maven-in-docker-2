package org.codejudge.sb.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import javax.validation.Valid;

import org.codejudge.sb.dto.ErrorUserResponse;
import org.codejudge.sb.dto.IUserResponse;
import org.codejudge.sb.dto.UserRequest;
import org.codejudge.sb.dto.UserResponse;
import org.codejudge.sb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Slf4j
public class AppController {

    @Autowired
    UserService myUserService;

    @ApiOperation("This is the hello world api")
    @GetMapping("/")
    public String hello() {
        return "Hello World!!";
    }

    @ApiOperation("This api is used to create the user")
    @PostMapping("/create")
    public ResponseEntity<IUserResponse> createUser(@Valid @RequestBody UserRequest myUserRequest)
    {
        log.info("Request received for create user");
        Optional<IUserResponse> response = myUserService.createUser(myUserRequest);
        if(response.isPresent())
        {
            return new ResponseEntity<>(response.get(),HttpStatus.CREATED);
        }
        return new ResponseEntity<>(ErrorUserResponse.builder().message("User already Exists").reason("User Already Exists").build(),HttpStatus.BAD_REQUEST);
    }


}
