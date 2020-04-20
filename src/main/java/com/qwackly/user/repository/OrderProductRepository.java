package com.qwackly.user.repository;

import com.qwackly.user.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, String> {

    OrderProduct findById(Integer id);
}
