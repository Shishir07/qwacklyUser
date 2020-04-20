package com.qwackly.user.service;

import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.UserEntity;
import com.qwackly.user.repository.OrderRepository;
import com.qwackly.user.enums.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static final String ORDER_NOT_ADDED="Order not added yet";

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserEntity userEntity;

    public List<OrderEntity> getAllOrders(){
        return orderRepository.findAll();
    }

    public void addOrder(OrderEntity orderEntity){
        orderRepository.save(orderEntity);
    }

    public OrderEntity getOrder(String id){
        Optional<OrderEntity> orderEntity = orderRepository.findById(id);
        if(orderEntity.isPresent())
            return orderEntity.get();
        else
            throw new QwacklyException(ORDER_NOT_ADDED, HttpStatus.NOT_FOUND.value(),ResponseStatus.FAILURE);
    }

    public List<OrderEntity> getAllOrdersForUser(Integer userId){
        userEntity.setId(userId);
        return orderRepository.findByUserEntity(userEntity);
    }

    public List<OrderEntity> getCartforUser(Integer userId,String status){
        userEntity.setId(userId);
        return orderRepository.findByUserEntityAndState(userEntity,status);
    }


}
