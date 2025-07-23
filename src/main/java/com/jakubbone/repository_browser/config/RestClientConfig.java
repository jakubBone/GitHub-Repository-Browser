package com.jakubbone.repository_browser.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${API_TOKEN}")
    private String apiToken;

    @Bean
    public RestClient restClient(){
        return RestClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader("Autorization","Bearer " + apiToken)
                .build();
    }
}
