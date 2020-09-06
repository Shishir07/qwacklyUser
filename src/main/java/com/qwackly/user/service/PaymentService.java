package com.qwackly.user.service;

import com.qwackly.user.enums.EmailStatus;
import com.qwackly.user.enums.OrderStatus;
import com.qwackly.user.enums.ResponseStatus;
import com.qwackly.user.exception.QwacklyException;
import com.qwackly.user.model.*;
import com.qwackly.user.repository.PaymentRepository;
import com.qwackly.user.request.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class PaymentService {

    @Value( "${cashfree.appId}" )
    private String appId;

    @Value("${cashfree.secretKey}")
    private String secretKey;

    @Value("${cashfree.postUrl}")
    private String postUrl;

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

    private static final String PENNDING_PAYMENT = "PENDING_PAYMENT";


    public String getSignature(MultiValueMap<String, String> paymentRequest) throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, String> postData = new HashMap<>();
        String orderId = String.valueOf(paymentRequest.get("orderId").get(0));
        String orderAmount = String.valueOf(paymentRequest.get("orderAmount").get(0));
        String phoneNumber = String.valueOf(paymentRequest.get("customerPhone").get(0));
        String userId = String.valueOf(paymentRequest.get("customerId").get(0));
        String customerName = String.valueOf(paymentRequest.get("customerName").get(0));
        String customerEmail = String.valueOf(paymentRequest.get("customerEmail").get(0));
        OrderEntity orderEntity = orderService.getOrder(orderId);
        return getEncodedSignature(postData, orderId, orderAmount, phoneNumber, userId, customerName, customerEmail, orderEntity);
    }

    public String getSignature2(PaymentRequest paymentRequest, String userId) throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, String> postData = new HashMap<>();
        String orderId = paymentRequest.getOrderId();
        String orderAmount = paymentRequest.getOrderAmount();
        String phoneNumber = paymentRequest.getCustomerPhone();
        String customerName = paymentRequest.getCustomerName();
        String customerEmail = paymentRequest.getCustomerEmail();
        OrderEntity orderEntity = orderService.getOrder(orderId);
        return getEncodedSignature(postData, orderId, orderAmount, phoneNumber, userId, customerName, customerEmail, orderEntity);
    }

    public HttpEntity<MultiValueMap<String, String>> getPayload(MultiValueMap<String, String> paymentRequest, String signature) {
        MultiValueMap<String, String> postData = new LinkedMultiValueMap<>();
        postData.add("appId", appId);
        postData.add("orderId", String.valueOf(paymentRequest.get("orderId").get(0)));
        postData.add("orderAmount", String.valueOf(paymentRequest.get("orderAmount").get(0)));
        postData.add("orderCurrency", "INR");
        postData.add("orderNote", "Qwackly Payments");
        postData.add("customerName", String.valueOf(paymentRequest.get("customerName").get(0)));
        postData.add("customerEmail", String.valueOf(paymentRequest.get("customerEmail").get(0)));
        postData.add("customerPhone", String.valueOf(paymentRequest.get("customerPhone").get(0)));
        postData.add("returnUrl", callBackUrl);
        postData.add("notifyUrl", notifyUrl);
        postData.add("signature", signature);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(postData, headers);
    }

    public HttpEntity<MultiValueMap<String, String>> getPayload2(PaymentRequest paymentRequest, String signature) {
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
        postData.add("signature", signature);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(postData, headers);
    }

    public ResponseEntity<String> makePaymentCallToCashfree(HttpEntity<MultiValueMap<String, String>> request) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(postUrl, request, String.class);
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

    private String getEncodedSignature(Map<String, String> postData, String orderId, String orderAmount, String phoneNumber, String userId, String customerName, String customerEmail, OrderEntity orderEntity) throws NoSuchAlgorithmException, InvalidKeyException {
        updateStateToPendingPayment(orderEntity);
        verifyOrderAmount(orderEntity, orderAmount);
        verifyUser(orderEntity, userId, customerName, customerEmail);
        updatePhoneNumber(phoneNumber, orderEntity);
        postData.put("appId", appId);
        postData.put("orderId", orderId);
        postData.put("orderAmount", orderAmount);
        postData.put("orderCurrency", "INR");
        postData.put("orderNote", "Qwackly Payments");
        postData.put("customerName", customerName);
        postData.put("customerEmail", customerEmail);
        postData.put("customerPhone", phoneNumber);
        postData.put("returnUrl", callBackUrl);
        postData.put("notifyUrl", notifyUrl);
        String data = "";
        SortedSet<String> keys = new TreeSet<String>(postData.keySet());
        for (String key : keys) {
            data = data + key + postData.get(key);
        }
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key_spec = new
                SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key_spec);
        return Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes()));
    }

    private void verifyOrderAmount(OrderEntity orderEntity, String orderAmount) {
        ProductEntity productEntity = orderProductService.findByOrderEntity(orderEntity).getProductEntity();
        if (!productEntity.getFinalPrice().toString().equalsIgnoreCase(orderAmount)){
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

    private void updateStateToPendingPayment(OrderEntity orderEntity) {
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
