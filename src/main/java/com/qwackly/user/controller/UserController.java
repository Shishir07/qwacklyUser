package com.qwackly.user.controller;

import com.qwackly.user.enums.VerificationType;
import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.model.UserEntity;
import com.qwackly.user.response.ListUserResponse;

import com.qwackly.user.service.UserService;
import com.qwackly.user.enums.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RequestMapping("/v1")
@RestController
public class UserController {

    public static final String USER_NOT_FOUND="User not found";

    @Autowired
    UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.GET )
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
    public ResponseEntity<UserEntity> getUser(@PathVariable Integer id){

        UserEntity userEntity;
        try{
            userEntity = userService.getUserDetails(id);
        }
        catch(Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<UserEntity> addUser(@RequestBody UserEntity userEntity){

        try{
            userService.addUser(userEntity);
        }
        catch(Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(userService.getUserDetails(userEntity.getId()), HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserEntity> modifyUser(@RequestBody UserEntity userEntity){

        try{
            if(Objects.nonNull(userService.getUserDetails(userEntity.getId())))
                userService.addUser(userEntity);
            else
                throw new QwacklyException(USER_NOT_FOUND, ResponseStatus.FAILURE);
        }
        catch(Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(userService.getUserDetails(userEntity.getId()), HttpStatus.OK);
    }


    @RequestMapping(value = "/users/{userid}/verify", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Boolean> verifyUser(@PathVariable Integer userId,@RequestBody String type) {
        UserEntity user;
        try {
            user=userService.getUserDetails(userId);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }

        return new ResponseEntity<>(VerificationType.get(type).isVerified(user),HttpStatus.OK);
    }

}
