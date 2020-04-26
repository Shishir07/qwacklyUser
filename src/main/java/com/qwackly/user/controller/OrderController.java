package com.qwackly.user.controller;

import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.OrderProduct;
import com.qwackly.user.response.ListOrderResponse;
import com.qwackly.user.service.OrderProductService;
import com.qwackly.user.service.OrderService;
import com.qwackly.user.enums.ResponseStatus;
import com.qwackly.user.service.ProductService;
import com.qwackly.user.util.OrderIdgenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    OrderProduct orderProduct;


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

    @GetMapping(value = "/users/{userid}/cart")
    public ResponseEntity<ListOrderResponse> getCart(@PathVariable Integer userId){
        List<OrderEntity> orderList;
        try {
            orderList=orderService.getCartforUser(userId,ADDED_TO_CART);
            listOrderResponse.setListOfOrders(orderList);
            listOrderResponse.setStatusCode(HttpStatus.OK.value());
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(listOrderResponse,HttpStatus.OK);
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<OrderEntity> addOrder(@RequestBody OrderEntity input, @RequestParam Integer productId){
        String orderId;
        try {
            orderId=orderIdgenerator.getUniqueOrderId();
            input.setId(orderId);
            orderProduct=new OrderProduct(ADDED_TO_CART,input,productService.getProduct(productId));
            orderProductService.addOrderProduct(orderProduct);
            orderService.addOrder(input);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(orderService.getOrder(orderId),HttpStatus.OK);
    }


    @RequestMapping(value = "/orders/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
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

    @RequestMapping(value = "/addToCart/products/{productId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<OrderEntity> addCart(@RequestBody OrderEntity input, @PathVariable Integer productId){
        String orderId;
        try {
            input.setState(ADDED_TO_CART);
            List<OrderEntity> orders= orderService.getCartforUser(input.getUserEntity().getId(),ADDED_TO_CART);
            if(orders.size()==0)
                orderId=orderIdgenerator.getUniqueOrderId();
            else
                orderId=orders.get(0).getId();
            input.setId(orderId);
            setOrderProductEntity(input, productId);
            orderProductService.addOrderProduct(orderProduct);
            orderService.addOrder(input);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(orderService.getOrder(orderId),HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteFromCart/products/{productId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<OrderEntity> deleteCart(@RequestBody OrderEntity input, @PathVariable Integer productId){
        String orderId;
        try {
            input.setState(ADDED_TO_CART);
            List<OrderEntity> orders= orderService.getCartforUser(input.getUserEntity().getId(),ADDED_TO_CART);
            if(orders.size()==0)
                orderId=orderIdgenerator.getUniqueOrderId();
            else
                orderId=orders.get(0).getId();
            input.setId(orderId);
            orderProduct=orderProductService.findByOrderandProduct(input,productService.getProduct(productId));
            orderProductService.deleteOrderProduct(orderProduct);
            orderService.addOrder(input);
        }
        catch (Exception e){
            throw new QwacklyException(e.getMessage(), ResponseStatus.FAILURE);
        }
        return new ResponseEntity<>(orderService.getOrder(orderId),HttpStatus.OK);
    }

    private void setOrderProductEntity( OrderEntity input,  Integer productId) {
        orderProduct.setState(ADDED_TO_CART);
        orderProduct.setProductEntity(productService.getProduct(productId));
        orderProduct.setOrderEntity(input);
    }

    public boolean removeCart(String orderId){
        OrderEntity order= orderService.getOrder(orderId);
        if(Objects.nonNull(order)){
            orderService.deleteOrder(order);
        }
        return true;
    }

}
