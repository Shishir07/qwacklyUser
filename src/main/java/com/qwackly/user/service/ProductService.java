package com.qwackly.user.service;

import com.qwackly.user.model.ProductEntity;
import com.qwackly.user.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<ProductEntity> getAllProducts(){
        return productRepository.findAll();
    }

    public List<ProductEntity> getAllAvailableProducts(){
        return productRepository.findByNoOfProductsGreaterThan(0);
    }

    public ProductEntity getProduct(Integer id){
        return productRepository.findById(id);
    }

    public void addProduct(ProductEntity productEntity){
        productRepository.save(productEntity);
    }
    public void updateProductNumber(ProductEntity productEntity, String status){
        if ("SUCCESS".equalsIgnoreCase(status) ) {
            productEntity.setNoOfProducts(productEntity.getNoOfProducts()-1);
            productRepository.save(productEntity);
        }
    }
}
