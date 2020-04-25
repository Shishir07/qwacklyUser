package com.qwackly.user.service;

import com.qwackly.user.dto.UserDetailsDto;
import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.exceptions.ResourceNotFoundException;
import com.qwackly.user.model.UserEntity;
import com.qwackly.user.repository.UserRepository;
import com.qwackly.user.enums.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    public static final String USER_NOT_FOUND="user not found";

    @Autowired
    UserRepository userRepository;

    public List<UserEntity> getAllUsers(){
        List<UserEntity> celebs=userRepository.findAll();
        return celebs;
    }

    public UserEntity getUserDetails(Integer userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    public void addUser(UserEntity userDetails){
        userRepository.save(userDetails);
    }

}
