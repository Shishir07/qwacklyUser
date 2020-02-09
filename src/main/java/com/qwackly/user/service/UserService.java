package com.qwackly.user.service;

import com.qwackly.user.dto.UserDetailsDto;
import com.qwackly.user.model.UserEntity;
import com.qwackly.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<UserEntity> getAllUsers(){
        List<UserEntity> celebs=userRepository.findAll();
        return celebs;
    }

    public UserDetailsDto getUserDetails(Integer id){
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        UserDetailsDto userDetails;
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            userDetails = new UserDetailsDto(user);
        }
        else {
            userDetails  = new UserDetailsDto();
        }
        return userDetails;

    }

    public void addUser(UserEntity userDetails){
        userRepository.save(userDetails);
    }

}
