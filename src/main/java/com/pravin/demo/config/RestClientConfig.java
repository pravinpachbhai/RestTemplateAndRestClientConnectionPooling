package com.pravin.demo.config;

import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean(destroyMethod = "close")
    public CloseableHttpClient pooledHttpClient() {

        // Connection pool
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(50);

        // TCP connection timeout
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                        .setConnectTimeout(Timeout.ofSeconds(5))
                        .build();

        connectionManager.setDefaultConnectionConfig(connectionConfig);

        // Apache HttpClient supports per-route configuration if you want different limits for different hosts.
        // This allows to define customize capacity for a specific destination.
        //connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("https", "payment-api.pravin.com", 443),
        //                                               new HttpHost("https", "Card-api.pravin.com", 443)), 100);

        // Request timeout configuration
        // Connection timeout: Time allowed to establish a TCP connection with the downstream server.
        // Connection request timeout: Time allowed to acquire an available connection from the pool.
        // Response timeout: Time allowed to wait for the response.
        RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectionRequestTimeout(Timeout.ofSeconds(3))
                        .setResponseTimeout(Timeout.ofSeconds(10))
                        .build();

        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .evictExpiredConnections()
                .evictIdleConnections(TimeValue.ofSeconds(30))
                .build();
    }

    @Bean
    public RestClient restClient(CloseableHttpClient pooledHttpClient) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(pooledHttpClient);
        return RestClient.builder().requestFactory(factory).build();
    }
}