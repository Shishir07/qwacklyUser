package com.qwackly.user.repository;

import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.OrderProductEntity;
import com.qwackly.user.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProductEntity, String> {

    OrderProductEntity findById(Integer id);
    OrderProductEntity findByOrderEntityAndProductEntity(OrderEntity orderEntity, ProductEntity productEntity);
}
