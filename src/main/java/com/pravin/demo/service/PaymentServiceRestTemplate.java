package com.pravin.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentServiceRestTemplate {

    private final RestTemplate restTemplate;

    public PaymentServiceRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String processPayment() {
        return restTemplate.getForObject("https://payment-api.pravin.com/payments", String.class);
    }
}