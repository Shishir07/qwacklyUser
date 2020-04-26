package com.qwackly.user.service;

import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.OrderProduct;
import com.qwackly.user.model.ProductEntity;
import com.qwackly.user.repository.OrderProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderProductService {

    @Autowired
    OrderProductRepository orderProductRepository;

    public void addOrderProduct(OrderProduct orderProduct){
        orderProductRepository.save(orderProduct);
    }

    public void deleteOrderProduct(OrderProduct orderProduct){
        orderProductRepository.delete(orderProduct);
    }

    public OrderProduct findOrderProduct(Integer id){
        return orderProductRepository.findById(id);
    }
    public OrderProduct findByOrderandProduct(OrderEntity orderEntity, ProductEntity productEntity){
        return orderProductRepository.findByOrderEntityAndProductEntity(orderEntity,productEntity);
    }
}
