package com.qwackly.user.controller;

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
    ListOrderResponse listOrderResponse;

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
    ApiResponse apiResponse;

    private static final String ADDED = "ADDED";

    private static final String PENNDING_PAYMENT = "PENDING_PAYMENT";

    private static  Map<String,String> response= new HashMap<>();

    @GetMapping(value = "/users/{userid}/orders")
    public ResponseEntity<ListOrderResponse> getOrders(@PathVariable Integer userId){
        List<OrderEntity> orderList;
        try {
            orderList=orderService.getAllOrdersForUser(userId);
            listOrderResponse.setListOfOrders(orderList);
            listOrderResponse.setStatusCode(HttpStatus.OK.value());
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE);
        }
            return new ResponseEntity<>(listOrderResponse,HttpStatus.OK);
    }

    @PostMapping(value = "/users/{userid}/orders")
    public ResponseEntity<ApiResponse> addOrder(@PathVariable Integer userId, @RequestBody Map<String, Object> payload){
        UserEntity userEntity= userService.getUserDetails(userId);
        ProductEntity productEntity=productService.getProduct((Integer) payload.get("productId"));
        WishListEntity wishListEntity = wishListService.findByProductEntity(productEntity);
        if(Objects.isNull(wishListEntity) || !wishListEntity.getStatus().equalsIgnoreCase(ADDED)){
            throw new QwacklyException("Product IS Not Available currently",ResponseStatus.FAILURE);
        }
        String orderId=orderIdgenerator.getUniqueOrderId();
        OrderEntity orderEntity = new OrderEntity(orderId,userEntity,PENNDING_PAYMENT);
        OrderProductEntity orderProductEntity= new OrderProductEntity(PENNDING_PAYMENT,orderEntity,productEntity);
        try{
            orderService.addOrder(orderEntity);
            orderProductService.addOrderProduct(orderProductEntity);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(),ResponseStatus.FAILURE);
        }
        apiResponse.setSuccess(true);
        apiResponse.setStatus("SUCCESS");
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
