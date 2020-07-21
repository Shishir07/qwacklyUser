package com.qwackly.user.repository;

import com.qwackly.user.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
    ProductEntity findById(Integer id);
    List<ProductEntity> findByStatusNot(String status);
}
