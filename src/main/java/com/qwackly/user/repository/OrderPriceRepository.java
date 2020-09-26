package com.qwackly.user.repository;

import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.OrderPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPriceRepository extends JpaRepository<OrderPriceEntity, Integer> {

    OrderPriceEntity findByOrderEntity(OrderEntity orderEntity);
}
