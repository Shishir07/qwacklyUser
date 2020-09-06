package com.qwackly.user.controller;


import com.google.gson.Gson;
import com.qwackly.user.model.EmailEntity;
import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.PaymentEntity;
import com.qwackly.user.request.PaymentRequest;
import com.qwackly.user.response.CashFreeCreateOrderResponse;
import com.qwackly.user.security.CurrentUser;
import com.qwackly.user.security.UserPrincipal;
import com.qwackly.user.service.EmailService;
import com.qwackly.user.service.OrderService;
import com.qwackly.user.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@RequestMapping("/v1")
@RestController
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @Autowired
    EmailService emailService;

    @Value("${qwackly.successRedirect}")
    private String successRedirect;

    @Value("${qwackly.failureRedirect}")
    private String failureRedirect;


    @PostMapping(value = "/payment",  consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CashFreeCreateOrderResponse> makePayment(@RequestBody PaymentRequest paymentRequest, @CurrentUser UserPrincipal userPrincipal) {
        CashFreeCreateOrderResponse cashFreeCreateOrderResponse = new CashFreeCreateOrderResponse();
        paymentService.verifyDetails(paymentRequest,userPrincipal.getId().toString());
        HttpEntity<MultiValueMap<String, String>> request = paymentService.getPayload(paymentRequest);
        ResponseEntity<String> response = paymentService.createOrderInCashfree(request, paymentRequest.getOrderId());
        if (response.getStatusCode().equals(HttpStatus.OK)){
            paymentService.updateStateToPendingPayment(paymentRequest.getOrderId());
            cashFreeCreateOrderResponse = new Gson().fromJson(response.getBody(), CashFreeCreateOrderResponse.class);
        }
        return new ResponseEntity<>(cashFreeCreateOrderResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/payment/callback", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void cashfreeCallback(@RequestBody MultiValueMap<String, String> cashfreeResponse, HttpServletResponse response) throws IOException {
        paymentService.savePayment(cashfreeResponse);
        if ("SUCCESS".equalsIgnoreCase(String.valueOf(cashfreeResponse.get("txStatus").get(0)))){
            response.sendRedirect(successRedirect);
        }
        else{
            response.sendRedirect(failureRedirect);
        }
    }

    @PostMapping(value = "/payment/notify", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void notifyPayment(@RequestBody MultiValueMap<String, String> cashfreeResponse) {
        paymentService.savePayment(cashfreeResponse);
    }
}