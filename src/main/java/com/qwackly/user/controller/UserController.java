package com.qwackly.user.controller;

import com.qwackly.user.dto.UserDetailsDto;
import com.qwackly.user.exceptions.ResourceNotFoundException;
import com.qwackly.user.model.UserEntity;
import com.qwackly.user.repository.UserRepository;
import com.qwackly.user.response.CommonAPIResponse;
import com.qwackly.user.response.ListUserResponse;
import com.qwackly.user.response.UserDetailResponse;

import com.qwackly.user.security.CurrentUser;
import com.qwackly.user.security.UserPrincipal;
import com.qwackly.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/users")
    public ResponseEntity<ListUserResponse> getUsers(){

        ListUserResponse listUserResponse = new ListUserResponse();
        List<UserEntity> userEntityList;
        try{
            userEntityList = userService.getAllUsers();
            listUserResponse.setListOfUsers(userEntityList);
            listUserResponse.setStatusCode(HttpStatus.OK.value());
        }
        catch(Exception e){
            listUserResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(listUserResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<UserDetailResponse> getUserDetail(@PathVariable ("id") Integer userId){

        UserDetailResponse userDetailResponse = new UserDetailResponse();
        UserDetailsDto userDetails;
        UserEntity user;
        try{
            userDetails = userService.getUserDetails(userId);
            userDetailResponse.setUserDetails(userDetails);
            userDetailResponse.setStatusCode(HttpStatus.OK.value());
        }
        catch (Exception e){
            userDetailResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(userDetailResponse, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserEntity getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }



}
