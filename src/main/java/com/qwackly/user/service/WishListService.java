package com.qwackly.user.service;

import com.qwackly.user.exceptions.ResourceNotFoundException;
import com.qwackly.user.model.ProductEntity;
import com.qwackly.user.model.UserEntity;
import com.qwackly.user.model.WishListEntity;
import com.qwackly.user.repository.WishListrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishListService {

    @Autowired
    WishListrepository wishListrepository;

    public List<WishListEntity> findAllProductsForUser(UserEntity userEntity){
        return wishListrepository.findByUserEntity(userEntity);
    }

    public void addProduct(WishListEntity wishListEntity){
        wishListrepository.save(wishListEntity);
    }

    public void deleteProduct(WishListEntity wishListEntity){
        wishListrepository.deleteById(wishListEntity.getId());
    }

    public WishListEntity getProductForAUser(UserEntity userEntity, ProductEntity productEntity){
        return wishListrepository.findByUserEntityAndProductEntity(userEntity,productEntity);
    }

    public WishListEntity findByProductEntity(ProductEntity productEntity){
        return wishListrepository.findByProductEntity(productEntity);
    }
}
