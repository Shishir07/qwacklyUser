package com.qwackly.user.controller;

import com.qwackly.user.enums.OrderStatus;
import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.model.*;
import com.qwackly.user.response.ApiResponse;
import com.qwackly.user.response.ListOrderResponse;
import com.qwackly.user.service.*;
import com.qwackly.user.enums.ResponseStatus;
import com.qwackly.user.util.OrderIdgenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/v1")
@RestController
public class OrderController {

    private static final String ADDED_TO_CART="ADDED_TO_CART";

    @Autowired
    OrderService orderService;

    @Autowired
    OrderIdgenerator orderIdgenerator;

    @Autowired
    ProductService productService;

    @Autowired
    OrderProductService orderProductService;

    @Autowired
    OrderProductEntity orderProductEntity;

    @Autowired
    WishListService wishListService;

    @Autowired
    UserService userService;

    private static final String ADDED = "ADDED";

    private static final String PENNDING_PAYMENT = "PENDING_PAYMENT";

    private static  Map<String,String> response= new HashMap<>();

    @RequestMapping(value = "/users/{userId}/orders/{status}" , method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ListOrderResponse> getOrders(@PathVariable Integer userId, @PathVariable String status){
        List<OrderProductEntity> orderList;
        ListOrderResponse listOrderResponse = new ListOrderResponse();
        try {
            orderList=orderProductService.getAllOrdersByUserAndStatus(userId,status);
            listOrderResponse.setListOfOrders(orderList);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE);
        }
            return new ResponseEntity<>(listOrderResponse,HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{userId}/orders", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> addOrder(@PathVariable Integer userId, @RequestBody Map<String, Integer> payload){
        UserEntity userEntity= userService.getUserDetails(userId);
        Integer productId = payload.get("productId");
        String orderId;
        ApiResponse apiResponse = new ApiResponse();
        ProductEntity productEntity=productService.getProduct(productId);
        WishListEntity wishListEntity = wishListService.findByProductEntity(productEntity);
        if(Objects.nonNull(wishListEntity) && !wishListEntity.getStatus().equalsIgnoreCase(ADDED)){
            throw new QwacklyException("Product IS Not Available currently",ResponseStatus.FAILURE);
        }
        OrderEntity order = orderService.findByProductIdIdAndUseId(productId,userId);
        if(Objects.nonNull(order)){
            orderId= order.getId();
        }
        else {
            orderId = orderIdgenerator.getUniqueOrderId();
            OrderEntity orderEntity = new OrderEntity(orderId, userEntity, OrderStatus.PENDING_PAYMENT);
            OrderProductEntity orderProductEntity = new OrderProductEntity(PENNDING_PAYMENT, orderEntity, productEntity);
            try {
                orderProductService.addOrderProduct(orderProductEntity);
                orderService.addOrder(orderEntity);
            } catch (Exception e) {
                throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
            }
        }
        apiResponse.setSuccess(true);
        apiResponse.setStatus("SUCCESS");
        apiResponse.setOrderId(orderId);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }


    @RequestMapping(value = "/orders/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<OrderEntity> getOrder(@PathVariable String id){
        OrderEntity order;
        try {
            order=orderService.getOrder(id);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(order,HttpStatus.OK);
    }

}
