package com.qwackly.user.repository;

import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {

    List<OrderEntity> findByUserEntity(UserEntity userEntity);
    List<OrderEntity> findByUserEntityAndState(UserEntity userEntity,String state);

}
