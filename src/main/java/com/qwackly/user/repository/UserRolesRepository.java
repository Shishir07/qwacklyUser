package com.qwackly.user.repository;

import com.qwackly.user.model.UserRolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRolesRepository extends JpaRepository<UserRolesEntity, Integer> {
    Optional<UserRolesEntity> findByRole (String role);
}
