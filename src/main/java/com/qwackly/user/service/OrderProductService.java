package com.qwackly.user.service;

import com.qwackly.user.model.OrderProduct;
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

    public OrderProduct findOrderProduct(Integer id){
        return orderProductRepository.findById(id);
    }
}
