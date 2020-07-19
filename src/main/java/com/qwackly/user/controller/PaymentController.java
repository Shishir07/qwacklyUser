package com.qwackly.user.controller;


import com.qwackly.user.model.OrderEntity;
import com.qwackly.user.model.PaymentEntity;
import com.qwackly.user.request.PaymentRequest;
import com.qwackly.user.service.OrderService;
import com.qwackly.user.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequestMapping("/v1")
@RestController
public class PaymentController {

    @Value("${qwackly.successRedirect}")
    private String successRedirect;

    @Value("${qwackly.failureRedirect}")
    private String failureRedirect;


    @Autowired
    PaymentService paymentService;


    @PostMapping(value = "/payment")
    public ResponseEntity<String> makePayment(@RequestBody PaymentRequest paymentRequest, HttpServletResponse servletresponse) throws IOException {
        ResponseEntity<String> response = null;
        try {
            String signature = paymentService.getSignature(paymentRequest);
            HttpEntity<MultiValueMap<String, String>> request = paymentService.getPayload(paymentRequest, signature);
            response = paymentService.makePaymentCallToCashfree(request);
        } catch (Exception e){
           servletresponse.sendRedirect(failureRedirect);
        }
        return response;
    }

    @PostMapping(value = "/payment/callback", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void cashfreeCallback(@RequestBody MultiValueMap<String, String> cashfreeResponse, HttpServletResponse response) throws IOException {
        paymentService.savePayment(cashfreeResponse);
        if ("SUCCESSFUL".equalsIgnoreCase(String.valueOf(cashfreeResponse.get("txStatus")))){
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