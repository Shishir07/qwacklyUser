package com.qwackly.user.service;

import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.OrderProductEntity;
import com.qwackly.user.model.ProductEntity;
import com.qwackly.user.repository.OrderProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderProductService {

    @Autowired
    OrderProductRepository orderProductRepository;

    public void addOrderProduct(OrderProductEntity orderProductEntity){
        orderProductRepository.save(orderProductEntity);
    }

    public void deleteOrderProduct(OrderProductEntity orderProductEntity){
        orderProductRepository.delete(orderProductEntity);
    }

    public OrderProductEntity findOrderProduct(Integer id){
        return orderProductRepository.findById(id);
    }
    public OrderProductEntity findByOrderAndProduct(OrderEntity orderEntity, ProductEntity productEntity){
        return orderProductRepository.findByOrderEntityAndProductEntity(orderEntity,productEntity);
    }
}
