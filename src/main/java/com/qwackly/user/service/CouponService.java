package com.qwackly.user.service;

import com.qwackly.user.enums.ResponseStatus;
import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.model.CouponEntity;
import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.OrderPriceEntity;
import com.qwackly.user.model.UserEntity;
import com.qwackly.user.repository.CouponRepository;
import com.qwackly.user.response.SuccessResponse;
import com.qwackly.user.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class CouponService {

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderPriceService orderPriceService;


    public void addCoupon(CouponEntity couponEntity){
        couponRepository.save(couponEntity);
    }

    public CouponEntity findByName(String couponName){
        return couponRepository.findByCouponName(couponName);
    }

    public SuccessResponse validateAndAddCoupon(Map<String, String> payload, UserPrincipal userPrincipal ){
        String orderId = payload.get("orderId");
        String couponName = payload.get("coupon");
        OrderEntity orderEntity = orderService.getOrder(orderId);
        validateUser(userPrincipal, orderEntity);
        CouponEntity couponEntity =findByName(couponName);
        validateCoupon(couponEntity);
        addDiscount(orderEntity, couponEntity);
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setSuccess(true);
        return  successResponse;
    }

    private void validateUser(UserPrincipal userPrincipal, OrderEntity orderEntity) {
        UserEntity userEntity = userService.getUserDetails(userPrincipal.getId());
        if (!orderEntity.getUserEntity().getEmailId().equalsIgnoreCase(userEntity.getEmailId())) {
            throw new QwacklyException("Invalid User", ResponseStatus.FAILURE);
        }
    }

    private void validateCoupon(CouponEntity couponEntity){
        if (Objects.isNull(couponEntity))
            throw new QwacklyException("Invalid Coupon", ResponseStatus.FAILURE);
    }

    private void addDiscount(OrderEntity orderEntity, CouponEntity couponEntity) {
        OrderPriceEntity orderPriceEntity = orderPriceService.findByOrdeEntity(orderEntity);
        Integer discount = (orderPriceEntity.getPrice() * couponEntity.getPercentage()) / 100;
        orderPriceEntity.setDiscount(discount);
        orderPriceService.addOrderPrice(orderPriceEntity);
    }

}

