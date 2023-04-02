package org.codejudge.sb.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import javax.validation.Valid;

import org.codejudge.sb.dto.ErrorUserResponse;
import org.codejudge.sb.dto.IUserResponse;
import org.codejudge.sb.dto.UserRequest;
import org.codejudge.sb.service.FriendRequestService;
import org.codejudge.sb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    FriendRequestService myFriendRequestService;

    @ApiOperation("This is the hello world api")
    @GetMapping("/")
    public String hello() {
        return "Hello World!!";
    }

    @ApiOperation("This api is used to create the user")
    @PostMapping("/create")
    public ResponseEntity<IUserResponse> createUser(@Valid @RequestBody UserRequest myUserRequest) {
        log.info("Request received for create user");
        Optional<IUserResponse> response = myUserService.createUser(myUserRequest);
        if (response.isPresent()) {
            return new ResponseEntity<>(response.get(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(ErrorUserResponse.builder().status("failure").reason("User Already Exists").build(),
                HttpStatus.BAD_REQUEST);
    }

    @ApiOperation("This api is used to create the user")
    @PostMapping("/add/{usera}/{userb}")
    public ResponseEntity<IUserResponse> sendFriendRequest(@PathVariable(name = "usera") String usera,
            @PathVariable(name = "userb") String userb) {
        log.info("Request received for friend request");
        Optional<IUserResponse> response = myFriendRequestService.createFriendRequest(usera, userb);
        if (response.isPresent())
            return new ResponseEntity<>(response.get(),
                    HttpStatus.ACCEPTED);
        return new ResponseEntity<>(ErrorUserResponse.builder().status("failure").reason("User Request Invalid").build(),
                HttpStatus.BAD_REQUEST);
    }

    @ApiOperation("This is the hello world api")
    @GetMapping("/friendRequests/{user}")
    public ResponseEntity<?> getFriendRequestForUser(@PathVariable(name="user") String user) {
        return new ResponseEntity<>(myFriendRequestService.getFriendRequestForUser(user).get(),HttpStatus.OK);
    }

    @ApiOperation("This is the hello world api")
    @GetMapping("/friends/{user}")
    public ResponseEntity<?> getFriendsUser(@PathVariable(name="user") String user) {
        return new ResponseEntity<>(myFriendRequestService.getFriendsForUser(user).get(),HttpStatus.OK);
    }

    @ApiOperation("This is the hello world api")
    @GetMapping("/suggestions/{user}")
    public ResponseEntity<?> getFriendSuggestions(@PathVariable(name="user") String user) {
        return new ResponseEntity<>(myFriendRequestService.getFriendsSuggestionsForUser(user).get(),HttpStatus.OK);
    }

}
