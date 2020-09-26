package com.qwackly.user.controller;

import com.qwackly.user.enums.ResponseStatus;
import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.OrderPriceEntity;
import com.qwackly.user.model.UserEntity;
import com.qwackly.user.response.ListOrderResponse;
import com.qwackly.user.security.CurrentUser;
import com.qwackly.user.security.UserPrincipal;
import com.qwackly.user.service.OrderPriceService;
import com.qwackly.user.service.OrderService;
import com.qwackly.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1")
@RestController
public class OrderPriceController {

    @Autowired
    OrderPriceService orderPriceService;

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/price/orders/{id}" , method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderPriceEntity> getPrice(@PathVariable String id, @CurrentUser UserPrincipal userPrincipal){
        OrderEntity orderEntity = orderService.getOrder(id);
        validateUser(userPrincipal, orderEntity);
        OrderPriceEntity orderPriceEntity = orderPriceService.findByOrdeEntity(orderEntity);
        return new ResponseEntity<>(orderPriceEntity, HttpStatus.OK);
    }

    private void validateUser(UserPrincipal userPrincipal, OrderEntity orderEntity) {
        UserEntity userEntity= userService.getUserDetails(userPrincipal.getId());
        if (!orderEntity.getUserEntity().getEmailId().equalsIgnoreCase(userEntity.getEmailId())){
            throw new QwacklyException("Invalid User", ResponseStatus.FAILURE);
        }
    }
}
