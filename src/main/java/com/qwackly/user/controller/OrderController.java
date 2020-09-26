package com.qwackly.user.controller;

import com.qwackly.user.enums.OrderStatus;
import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.model.*;
import com.qwackly.user.response.ApiResponse;
import com.qwackly.user.response.ListOrderResponse;
import com.qwackly.user.security.CurrentUser;
import com.qwackly.user.security.UserPrincipal;
import com.qwackly.user.service.*;
import com.qwackly.user.enums.ResponseStatus;
import com.qwackly.user.util.OrderIdgenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    OrderPriceService orderPriceService;

    private static final String ADDED = "ADDED";

    private static final String PENNDING_PAYMENT = "PENDING_PAYMENT";

    private static  Map<String,String> response= new HashMap<>();

    @RequestMapping(value = "/users/orders/{status}" , method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ListOrderResponse> getOrders(@PathVariable String status, @CurrentUser UserPrincipal userPrincipal){
        List<OrderProductEntity> orderList;
        ListOrderResponse listOrderResponse = new ListOrderResponse();
        Integer userId = userPrincipal.getId();
        try {
            orderList=orderProductService.getAllOrdersByUserAndStatus(userId,status);
            listOrderResponse.setListOfOrders(orderList);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE);
        }
            return new ResponseEntity<>(listOrderResponse,HttpStatus.OK);
    }

    @RequestMapping(value = "/users/orders", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> addOrder( @RequestBody Map<String, Integer> payload, @CurrentUser UserPrincipal userPrincipal ){
        Integer userId = userPrincipal.getId();
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
            orderId = order.getId();
        }
        else {
            orderId = orderIdgenerator.getUniqueOrderId();
            OrderEntity orderEntity = new OrderEntity(orderId, userEntity, OrderStatus.ADDED);
            OrderProductEntity orderProductEntity = new OrderProductEntity(ADDED, orderEntity, productEntity);
            OrderPriceEntity orderPriceEntity = new OrderPriceEntity(orderEntity,productEntity.getPrice());
            try {
                orderProductService.addOrderProduct(orderProductEntity);
                orderService.addOrder(orderEntity);
                orderPriceService.addOrderPrice(orderPriceEntity);
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
    public ResponseEntity<OrderEntity> getOrder(@PathVariable String id , @CurrentUser UserPrincipal userPrincipal){
        OrderEntity order;
        Integer userId = userPrincipal.getId();
        UserEntity userEntity = userService.getUserDetails(userId);
        try {
            order=orderService.getOrderByIdAndUserEntity(id,userEntity);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(order,HttpStatus.OK);
    }

}
