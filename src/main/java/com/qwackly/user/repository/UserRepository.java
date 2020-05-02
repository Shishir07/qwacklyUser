package com.qwackly.user.repository;

import com.qwackly.user.model.ProductEntity;
import com.qwackly.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmailId (String email);
}
