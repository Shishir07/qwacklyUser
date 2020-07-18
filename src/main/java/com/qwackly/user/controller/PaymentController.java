package com.qwackly.user.controller;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RequestMapping("/v1")
@RestController
public class PaymentController {


    @PostMapping(value = "/payment" )
    public ResponseEntity<String> makePayment() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Map<String, String> postData = new HashMap<String, String>();
        postData.put("appId", "21822fe95aa2ee53ddb52e21222812");
        postData.put("orderId", "1000399");
        postData.put("orderAmount", "1");
        postData.put("orderCurrency", "INR");
        postData.put("orderNote", "Bhag BHSDK");
        postData.put("customerName", "Abhinav");
        postData.put("customerEmail", "abhinav.k@qwackly.com");
        postData.put("customerPhone", "9005514417");
        postData.put("returnUrl","https://29da06fa6369.ngrok.io/");
        postData.put("notifyUrl", "https://29da06fa6369.ngrok.io/");
        String data = "";
        SortedSet<String> keys = new TreeSet<String>(postData.keySet());
        for (String key : keys) {
            data = data + key + postData.get(key);
        }
        String secretKey = "e56d02ea63d9976c4e13461b4ae1741d1b695859";
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key_spec = new
                SecretKeySpec(secretKey.getBytes(),"HmacSHA256");
        sha256_HMAC.init(secret_key_spec);
        String signature = Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes()));
        System.out.println("Signature is "+signature);
        postData.put("signature",signature);

        String       postUrl       = "https://test.cashfree.com/billpay/checkout/post/submit";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("appId", "21822fe95aa2ee53ddb52e21222812");
        map.add("orderId", "1000399");
        map.add("orderAmount", "1");
        map.add("orderCurrency", "INR");
        map.add("orderNote", "Bhag BHSDK");
        map.add("customerName", "Abhinav");
        map.add("customerEmail", "abhinav.k@qwackly.com");
        map.add("customerPhone", "9005514417");
        map.add("returnUrl","https://29da06fa6369.ngrok.io/");
        map.add("notifyUrl", "https://29da06fa6369.ngrok.io/");
        map.add("signature",signature);

        org.springframework.http.HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response101 = restTemplate.postForEntity( postUrl, request , String.class );
        return response101;
    }

    public static void main(String args[]) throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, String> postData = new HashMap<String, String>();
        postData.put("appId", "21822fe95aa2ee53ddb52e21222812");
        postData.put("orderId", "1000354");
        postData.put("orderAmount", "1");
        postData.put("orderCurrency", "INR");
        postData.put("orderNote", "Bhag BHSDK");
        postData.put("customerName", "Abhinav");
        postData.put("customerEmail", "abhinav.k@qwackly.com");
        postData.put("customerPhone", "9005514417");
        postData.put("returnUrl","https://localhost:8080/users");
        postData.put("notifyUrl", "https://localhost:8080/users");
        String data = "";
        SortedSet<String> keys = new TreeSet<String>(postData.keySet());
        for (String key : keys) {
            data = data + key + postData.get(key);
        }
        String secretKey = "e56d02ea63d9976c4e13461b4ae1741d1b695859";
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key_spec = new
                SecretKeySpec(secretKey.getBytes(),"HmacSHA256");
        sha256_HMAC.init(secret_key_spec);
        String signature = Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes()));
        System.out.println("Signature is "+signature);
    }
}
