package com.pravin.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PaymentServiceWithRestClient {

    private final RestClient restClient;

    public PaymentServiceWithRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public String getPaymentDetails(String paymentId) {

        return restClient.get()
                .uri("https://payment-api.pravin.com/payments/{id}", paymentId)
                .retrieve()
                .body(String.class);
    }
}