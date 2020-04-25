package com.qwackly.user.service;

import com.qwackly.user.model.UserEntity;
import com.qwackly.user.model.UserRolesEntity;
import com.qwackly.user.repository.UserRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {

    @Autowired
    UserRolesRepository userRolesRepository;

    public List<UserRolesEntity> findUserRolesByUser(UserEntity userEntity){
        return userRolesRepository.findByUser(userEntity);
    }
}
