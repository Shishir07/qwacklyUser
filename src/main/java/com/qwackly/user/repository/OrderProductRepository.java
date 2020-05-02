package com.qwackly.user.repository;

import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.OrderProduct;
import com.qwackly.user.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, String> {

    OrderProduct findById(Integer id);
    OrderProduct findByOrderEntityAndProductEntity(OrderEntity orderEntity, ProductEntity productEntity);
}
