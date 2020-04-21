package com.qwackly.user.repository;

import com.qwackly.user.model.ProductEntity;
import com.qwackly.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findById(Integer id);

}
