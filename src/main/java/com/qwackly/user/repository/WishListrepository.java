package com.qwackly.user.repository;

import com.qwackly.user.model.ProductEntity;
import com.qwackly.user.model.UserEntity;
import com.qwackly.user.model.WishListEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WishListrepository extends JpaRepository<WishListEntity, Integer> {

    List<WishListEntity> findByUserEntity(UserEntity userEntity);
    WishListEntity findByUserEntityAndProductEntity(UserEntity userEntity, ProductEntity productEntity);
    WishListEntity findByProductEntity(ProductEntity productEntity);

    @Modifying
    @Transactional
    @Query(value = "delete from wishlist w where w.id = :id",  nativeQuery = true)
    void deleteById(Integer id);
}
