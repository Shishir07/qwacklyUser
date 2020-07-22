package com.qwackly.user.repository;

import com.qwackly.user.enums.OrderStatus;
import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {

    List<OrderEntity> findByUserEntity(UserEntity userEntity);
    //List<OrderEntity> findByUserEntityAndState(UserEntity userEntity,String state);
    @Query(value = "select * from orders where user_id = :userId and id in (select order_id from order_products where product_id = :productId) and status = 'PENDING_PAYMENT'", nativeQuery = true)
    OrderEntity findByProductIdIdAndUseId(@Param("productId") Integer productId, @Param("userId") Integer userId);
    List<OrderEntity> findByUserEntityAndState(UserEntity userEntity, OrderStatus status);
}
