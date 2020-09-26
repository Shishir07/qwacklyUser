package com.qwackly.user.service;

import com.qwackly.user.enums.EmailStatus;
import com.qwackly.user.enums.OrderStatus;
import com.qwackly.user.enums.ResponseStatus;
import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.model.*;
import com.qwackly.user.repository.PaymentRepository;
import com.qwackly.user.request.PaymentRequest;
import com.qwackly.user.response.CashFreeCreateOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import com.google.gson.*;

@Service
public class PaymentService {

    @Value( "${cashfree.appId}" )
    private String appId;

    @Value("${cashfree.secretKey}")
    private String secretKey;

    @Value("${cashfree.postUrl}")
    private String postUrl;

    @Value("${cashfree.paymentLink}")
    private String infoLinkUrl;

    @Value("${cashfree.callbackUrl}")
    private String callBackUrl;

    @Value("${cashfree.notifyUrl}")
    private String notifyUrl;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderProductService orderProductService;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    OrderPriceService orderPriceService;

    private static final String PENNDING_PAYMENT = "PENDING_PAYMENT";

    public void verifyDetails(PaymentRequest paymentRequest, String userId) {
        String orderId = paymentRequest.getOrderId();
        OrderEntity orderEntity = orderService.getOrder(orderId);
        verifyOrderAmount(orderEntity, paymentRequest.getOrderAmount());
        verifyUser(orderEntity, userId, paymentRequest.getCustomerName(), paymentRequest.getCustomerEmail());
        updatePhoneNumber(paymentRequest.getCustomerPhone(), orderEntity);
    }

    public HttpEntity<MultiValueMap<String, String>> getPayload(PaymentRequest paymentRequest) {
        MultiValueMap<String, String> postData = new LinkedMultiValueMap<>();
        postData.add("appId", appId);
        postData.add("orderId", paymentRequest.getOrderId());
        postData.add("orderAmount", paymentRequest.getOrderAmount());
        postData.add("orderCurrency", "INR");
        postData.add("orderNote", "Qwackly Payments");
        postData.add("customerName", paymentRequest.getCustomerName());
        postData.add("customerEmail", paymentRequest.getCustomerEmail());
        postData.add("customerPhone", paymentRequest.getCustomerPhone());
        postData.add("returnUrl", callBackUrl);
        postData.add("notifyUrl", notifyUrl);
        postData.add("secretKey", secretKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(postData, headers);
    }

    public ResponseEntity<String> createOrderInCashfree(HttpEntity<MultiValueMap<String, String>> request, String orderId) {
        OrderEntity orderEntity = orderService.getOrder(orderId);
        ResponseEntity<String> response;
        RestTemplate restTemplate = new RestTemplate();
        if (orderEntity.getState().equals(OrderStatus.PENDING_PAYMENT))
            response = restTemplate.postForEntity(infoLinkUrl, request, String.class);
        else
            response = restTemplate.postForEntity(postUrl, request, String.class);
        return response;
    }

    public void savePayment(MultiValueMap<String, String> cashfreeResponse){
        PaymentEntity paymentEntity = new PaymentEntity();
        OrderEntity orderEntity = orderService.getOrder(String.valueOf(cashfreeResponse.get("orderId").get(0)));
        OrderProductEntity orderProductEntity = orderProductService.findByOrderEntity(orderEntity);
        String paymentStatus = String.valueOf(cashfreeResponse.get("txStatus").get(0));
        String paymentMode = String.valueOf(cashfreeResponse.get("paymentMode").get(0));
        String reference = String.valueOf(cashfreeResponse.get("referenceId").get(0));
        String transactionMessage = String.valueOf(cashfreeResponse.get("txMsg").get(0));
        PaymentEntity existingPayment = paymentRepository.findByOrderEntity(orderEntity);
        String orderAmount = String.valueOf(cashfreeResponse.get("orderAmount").get(0));
        if (Objects.nonNull(existingPayment)){
            existingPayment.setPaymentStatus(paymentStatus);
            existingPayment.setPaymentMode(paymentMode);
            existingPayment.setReferenceId(reference);
            existingPayment.setTransactionMessage(transactionMessage);
            paymentRepository.save(existingPayment);
        }
        else {
            paymentEntity.setOrderEntity(orderEntity);
            paymentEntity.setAmount(orderAmount);
            paymentEntity.setPaymentMode(paymentMode);
            paymentEntity.setReferenceId(reference);
            paymentEntity.setPaymentStatus(paymentStatus);
            paymentEntity.setTransactionMessage(transactionMessage);
            paymentRepository.save(paymentEntity);
        }
        orderService.updateOrderState(orderEntity,paymentStatus);
        orderProductService.updateOrderProductState(orderProductEntity,paymentStatus);
        addEmailEntity(orderEntity,orderAmount);
    }

    private void verifyOrderAmount(OrderEntity orderEntity, String orderAmount) {
        OrderPriceEntity priceEntity = orderPriceService.findByOrdeEntity(orderEntity);
        if (!priceEntity.getFinalPrice().toString().equalsIgnoreCase(orderAmount)){
            throw new QwacklyException("Order amount does not match with the price of the product", ResponseStatus.FAILURE);
        }
    }

    private void verifyUser(OrderEntity orderEntity, String userId, String firstName, String emailId){
        UserEntity userEntity = orderEntity.getUserEntity();
        if (!(userEntity.getId().toString().equalsIgnoreCase(userId) && userEntity.getFirstName().equalsIgnoreCase(firstName) && userEntity.getEmailId().equalsIgnoreCase(emailId))){
            throw new QwacklyException("User Details does not match for this order", ResponseStatus.FAILURE);
        }
    }

    private void updatePhoneNumber( String phoneNumber, OrderEntity orderEntity) {
        UserEntity userEntity = orderEntity.getUserEntity();
        if (Objects.isNull(userEntity.getPhoneNumber()) || !userEntity.getPhoneNumber().equalsIgnoreCase(phoneNumber)){
            userEntity.setPhoneNumber(phoneNumber);
            userService.addUser(userEntity);
        }
    }

    public void updateStateToPendingPayment(String orderId) {
        OrderEntity orderEntity = orderService.getOrder(orderId);
        orderEntity.setState(OrderStatus.PENDING_PAYMENT);
        OrderProductEntity orderProductEntity = orderProductService.findByOrderEntity(orderEntity);
        orderProductEntity.setState(PENNDING_PAYMENT);
        orderService.addOrder(orderEntity);
        orderProductService.addOrderProduct(orderProductEntity);
    }

    private void addEmailEntity(OrderEntity orderEntity, String orderAmount){
        UserEntity userEntity = orderEntity.getUserEntity();
        String customerEmail = userEntity.getEmailId();
        String customerName = userEntity.getFirstName();
        EmailEntity emailEntity = new EmailEntity(orderEntity,orderAmount,customerEmail,customerName, EmailStatus.PENDING_SEND);
        emailService.addEmailEntity(emailEntity);
    }
}
