package com.jakubbone.repository_browser.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${api.token}")
    private String apiToken;

    @Value("${github.api.base-url}")
    private String baseUrl;

    @Bean
    public RestClient restClient(){
        if (apiToken == null || apiToken.trim().isEmpty()) {
            throw new IllegalStateException("API_TOKEN must be provided");
        }

        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization","Bearer " + apiToken)
                .build();
    }
}
