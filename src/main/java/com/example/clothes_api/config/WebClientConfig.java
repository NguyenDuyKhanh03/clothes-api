package com.example.clothes_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClientGHN(WebClient.Builder builder) {
        return builder.baseUrl("https://online-gateway.ghn.vn/shiip/public-api").build();
    }

    @Bean
    public WebClient webClientZalopay(WebClient.Builder builder) {
        return builder.baseUrl("https://sandbox.zalopay.com.vn/v001/tpe").build();
    }
}
