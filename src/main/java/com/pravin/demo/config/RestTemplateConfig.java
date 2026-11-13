package com.pravin.demo.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class RestTemplateConfig {

/*
    Parameter	                   Example	            Meaning
    MaxTotal	                   200	                Maximum total connections
    MaxPerRoute	                   50	                Maximum connections to one host
    ConnectionRequestTimeout	   3 sec	            Maximum wait for a free connection
    ResponseTimeout	               10 sec	            Maximum wait for response
    Idle eviction	               30 sec	            Close idle connections after this duration
 */


    @Bean
    public PoolingHttpClientConnectionManager connectionManager() {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        // Maximum connections across all destinations
        manager.setMaxTotal(200);
        // Maximum connections per destination
        manager.setDefaultMaxPerRoute(50);
        return manager;
    }

    @Bean(destroyMethod = "close")
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager manager) {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(3, TimeUnit.SECONDS)
                .setResponseTimeout(10, TimeUnit.SECONDS)
                .build();

        return HttpClients.custom()
                .setConnectionManager(manager)
                .setDefaultRequestConfig(requestConfig)
                .evictExpiredConnections()
                .evictIdleConnections(TimeValue.ofSeconds(30))
                .build();
    }

    @Bean
    public RestTemplate restTemplate(CloseableHttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }

}
