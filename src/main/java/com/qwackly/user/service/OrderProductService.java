package com.qwackly.user.service;

import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.OrderProductEntity;
import com.qwackly.user.model.ProductEntity;
import com.qwackly.user.model.UserEntity;
import com.qwackly.user.repository.OrderProductRepository;
import com.qwackly.user.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderProductService {

    @Autowired
    OrderProductRepository orderProductRepository;
    @Autowired
    UserService userService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductService productService;

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

    public List<OrderProductEntity> getAllOrdersForUser(Integer userId){
        UserEntity userEntity=userService.getUserDetails(userId);
        List<OrderEntity> orderList= orderRepository.findByUserEntity(userEntity);
        List<OrderProductEntity> orderProductEntities = new ArrayList<>();
        for(OrderEntity orderEntity: orderList){
            OrderProductEntity orderProductEntity = orderProductRepository.findByOrderEntity(orderEntity);
            orderProductEntities.add(orderProductEntity);
        }
        return orderProductEntities;
    }

    public OrderProductEntity findByOrderEntity(OrderEntity orderEntity){
        return orderProductRepository.findByOrderEntity(orderEntity);
    }

    public void updateOrderProductState(OrderProductEntity orderProductEntity, String state){
        ProductEntity productEntity = orderProductEntity.getProductEntity();
        if (!"SUCCESS".equalsIgnoreCase(orderProductEntity.getState())){
            orderProductEntity.setState(state);
            orderProductRepository.save(orderProductEntity);
            productService.updateProductNumber(productEntity,state);
        }
    }
}
