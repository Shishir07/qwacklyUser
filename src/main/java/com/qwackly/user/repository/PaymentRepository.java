package com.qwackly.user.repository;

import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {
        PaymentEntity findByOrderEntity(OrderEntity orderEntity);
}
