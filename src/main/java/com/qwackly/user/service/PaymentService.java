package com.qwackly.user.service;

import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.OrderProductEntity;
import com.qwackly.user.model.PaymentEntity;
import com.qwackly.user.model.ProductEntity;
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
import org.springframework.web.bind.annotation.RequestBody;
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

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderProductService orderProductService;

    @Autowired
    ProductService productService;

    private static final String CASHFREE_CALLBACK_URL = "http://development.qwackly.in:8089/v1/payment/callback";
    private static final String CASHFREE_NOTIFY_URL = "http://development.qwackly.in:8089/v1/payment/notify";

    public String getSignature(MultiValueMap<String, String> paymentRequest) throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, String> postData = new HashMap<>();
        postData.put("appId", appId);
        postData.put("orderId", String.valueOf(paymentRequest.get("orderId").get(0)));
        postData.put("orderAmount", String.valueOf(paymentRequest.get("orderAmount").get(0)));
        postData.put("orderCurrency", "INR");
        postData.put("orderNote", "Qwackly Payments");
        postData.put("customerName", String.valueOf(paymentRequest.get("customerName").get(0)));
        postData.put("customerEmail", String.valueOf(paymentRequest.get("customerEmail").get(0)));
        postData.put("customerPhone", String.valueOf(paymentRequest.get("customerPhone").get(0)));
        postData.put("returnUrl", CASHFREE_CALLBACK_URL);
        postData.put("notifyUrl", CASHFREE_NOTIFY_URL);
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
        postData.add("returnUrl", CASHFREE_CALLBACK_URL);
        postData.add("notifyUrl", CASHFREE_NOTIFY_URL);
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
        if (Objects.nonNull(existingPayment)){
            existingPayment.setPaymentStatus(paymentStatus);
            existingPayment.setPaymentMode(paymentMode);
            existingPayment.setReferenceId(reference);
            existingPayment.setTransactionMessage(transactionMessage);
            paymentRepository.save(existingPayment);
        }
        else {
            paymentEntity.setOrderEntity(orderEntity);
            paymentEntity.setAmount(String.valueOf(cashfreeResponse.get("orderAmount").get(0)));
            paymentEntity.setPaymentMode(paymentMode);
            paymentEntity.setReferenceId(reference);
            paymentEntity.setPaymentStatus(paymentStatus);
            paymentEntity.setTransactionMessage(transactionMessage);
            paymentRepository.save(paymentEntity);
        }
        orderService.updateOrderState(orderEntity,paymentStatus);
        orderProductService.updateOrderProductState(orderProductEntity,paymentStatus);
    }
}
