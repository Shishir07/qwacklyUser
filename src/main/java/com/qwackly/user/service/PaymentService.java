package com.qwackly.user.service;

import com.qwackly.user.model.OrderEntity;
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
    ProductService productService;

    public String getSignature(PaymentRequest paymentRequest) throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, String> postData = new HashMap<>();
        postData.put("appId", appId);
        postData.put("orderId", paymentRequest.getOrderId());
        postData.put("orderAmount", String.valueOf(paymentRequest.getOrderAmount()));
        postData.put("orderCurrency", "INR");
        postData.put("orderNote", "Qwackly Payments");
        postData.put("customerName", paymentRequest.getCustomerName());
        postData.put("customerEmail", paymentRequest.getCustomerEmail());
        postData.put("customerPhone", paymentRequest.getCustomerPhone());
        postData.put("returnUrl", "http://localhost:8080/v1/payment/callback");
        postData.put("notifyUrl", "http://localhost:8080/v1/payment/notify");
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

    public HttpEntity<MultiValueMap<String, String>> getPayload(@RequestBody PaymentRequest paymentRequest, String signature) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("appId", appId);
        map.add("orderId", paymentRequest.getOrderId());
        map.add("orderAmount", String.valueOf(paymentRequest.getOrderAmount()));
        map.add("orderCurrency", "INR");
        map.add("orderNote", "Qwackly Payments");
        map.add("customerName", paymentRequest.getCustomerName());
        map.add("customerEmail", paymentRequest.getCustomerEmail());
        map.add("customerPhone", paymentRequest.getCustomerPhone());
        map.add("returnUrl", "http://localhost:8080/v1/payment/callback");
        map.add("notifyUrl", "http://localhost:8080/v1/payment/notify");
        map.add("signature", signature);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(map, headers);
    }

    public ResponseEntity<String> makePaymentCallToCashfree(HttpEntity<MultiValueMap<String, String>> request) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(postUrl, request, String.class);
    }

    public void savePayment(MultiValueMap<String, String> cashfreeResponse){
        PaymentEntity paymentEntity = new PaymentEntity();
        OrderEntity orderEntity = orderService.getOrder(String.valueOf(cashfreeResponse.get("orderId")));
        String paymentStatus = String.valueOf(cashfreeResponse.get("txStatus"));
        String paymentMode = String.valueOf(cashfreeResponse.get("paymentMode"));
        String reference = String.valueOf(cashfreeResponse.get("referenceId"));
        PaymentEntity existingPayment = paymentRepository.findByOrderEntity(orderEntity);
        if (Objects.nonNull(existingPayment)){
            existingPayment.setPaymentStatus(paymentStatus);
            existingPayment.setPayMentMode(paymentMode);
            existingPayment.setReferenceId(reference);
            paymentRepository.save(existingPayment);
        }
        else {
            paymentEntity.setOrderEntity(orderEntity);
            paymentEntity.setAmount(String.valueOf(cashfreeResponse.get("orderAmount")));
            paymentEntity.setPayMentMode(paymentMode);
            paymentEntity.setReferenceId(reference);
            paymentEntity.setPaymentStatus(paymentStatus);
            paymentRepository.save(paymentEntity);
        }
    }
}
