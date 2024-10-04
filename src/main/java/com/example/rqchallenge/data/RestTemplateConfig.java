package com.example.rqchallenge.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate dummyApiRestTemplate(@Value("${dummy-api.base-url}") String baseUrl,
                                             @Value("${dummy-api.username}") String username,
                                             @Value("${dummy-api.password}") String password,
                                             @Value("${dummy-api.api-version}") String apiVersion,
                                             RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .rootUri(baseUrl + apiVersion)
                .basicAuthentication(username, password)
                .build();
    }
}
