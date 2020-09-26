package com.qwackly.user.controller;

import com.qwackly.user.enums.ResponseStatus;
import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.model.CouponEntity;
import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.OrderPriceEntity;
import com.qwackly.user.model.UserEntity;
import com.qwackly.user.response.ApiResponse;
import com.qwackly.user.response.SuccessResponse;
import com.qwackly.user.security.CurrentUser;
import com.qwackly.user.security.UserPrincipal;
import com.qwackly.user.service.CouponService;
import com.qwackly.user.service.OrderPriceService;
import com.qwackly.user.service.OrderService;
import com.qwackly.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RequestMapping("/v1")
@RestController
public class CouponController {

    @Autowired
    CouponService couponService;

    @Autowired
    OrderPriceService orderPriceService;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/coupons", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> addCoupon(@RequestBody Map<String, String> payload, @CurrentUser UserPrincipal userPrincipal) {
        SuccessResponse successResponse = couponService.validateAndAddCoupon(payload,userPrincipal);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }
}
