package com.qwackly.user.repository;

import com.qwackly.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByCountry(String country);
}
