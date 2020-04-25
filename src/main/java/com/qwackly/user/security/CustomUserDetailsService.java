package com.qwackly.user.security;


import com.qwackly.user.exceptions.ResourceNotFoundException;
import com.qwackly.user.model.UserEntity;
import com.qwackly.user.model.UserRolesEntity;
import com.qwackly.user.repository.UserRepository;
import com.qwackly.user.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRoleService userRoleService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmailId(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + email)
        );
        List<UserRolesEntity> userRolesEntities=userRoleService.findUserRolesByUser(user);
        return UserPrincipal.create(user,userRolesEntities);
    }

    @Transactional
    public UserDetails loadUserById(Integer id) {
        UserEntity user = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("User", "id", id)
        );
        List<UserRolesEntity> userRolesEntities=userRoleService.findUserRolesByUser(user);
        return UserPrincipal.create(user,userRolesEntities);
    }
}