package com.qwackly.user.dto;

import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.service.OrderLifeCycleService;
import com.qwackly.user.service.OrderProductService;
import com.qwackly.user.service.OrderService;
import com.qwackly.user.service.WishListService;
import lombok.Data;

@Data
public class StateContextDto {
    String orderId;
    OrderEntity orderEntity;
    OrderService orderService;
    OrderProductService orderProductService;
    WishListService wishListService;
    OrderLifeCycleService orderLifeCycleService;
}
