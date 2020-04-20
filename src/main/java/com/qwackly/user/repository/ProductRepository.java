package com.qwackly.user.repository;

import com.qwackly.user.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
    ProductEntity findById(Integer id);
}
