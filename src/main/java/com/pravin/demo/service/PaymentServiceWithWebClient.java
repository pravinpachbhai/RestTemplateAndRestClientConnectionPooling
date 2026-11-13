package com.pravin.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PaymentServiceWithWebClient {

    private final WebClient webClient;

    public PaymentServiceWithWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> getPayment(String paymentId) {

        return webClient.get()
                .uri("https://payment-api.example.com/payments/{id}", paymentId)
                .retrieve()
                .bodyToMono(String.class);
    }
}