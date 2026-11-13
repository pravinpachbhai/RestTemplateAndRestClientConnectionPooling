package com.pravin.demo.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class WebClientConfig {

    @Bean(destroyMethod = "dispose")
    public ConnectionProvider connectionProvider() {

        return ConnectionProvider.builder("payment-pool")
                .maxConnections(200)
                .pendingAcquireMaxCount(500)
                .pendingAcquireTimeout(Duration.ofSeconds(3))
                .maxIdleTime(Duration.ofSeconds(30))
                .maxLifeTime(Duration.ofMinutes(5))
                .evictInBackground(Duration.ofSeconds(60))
                .metrics(true)
                .build();
    }

    @Bean
    public WebClient webClient(ConnectionProvider connectionProvider) {
        HttpClient httpClient = HttpClient.create(connectionProvider).responseTimeout(Duration.ofSeconds(10));
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }
}