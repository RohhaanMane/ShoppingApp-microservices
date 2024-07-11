package com.rohan.order_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced                           // adds client side load balancing to WebClient builder
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }


}
