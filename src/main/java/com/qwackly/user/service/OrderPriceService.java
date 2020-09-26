package com.qwackly.user.service;

import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.OrderPriceEntity;
import com.qwackly.user.repository.OrderPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderPriceService {

    @Autowired
    OrderPriceRepository orderPriceRepository;

    public void addOrderPrice(OrderPriceEntity orderPriceEntity){
        orderPriceRepository.save(orderPriceEntity);
    }

    public OrderPriceEntity findById(Integer id){
        Optional<OrderPriceEntity> orderPriceEntity = orderPriceRepository.findById(id);
        return orderPriceEntity.isPresent() ? orderPriceEntity.get() : null;
    }

    public OrderPriceEntity findByOrdeEntity(OrderEntity orderEntity){
        return orderPriceRepository.findByOrderEntity(orderEntity);
    }
}
