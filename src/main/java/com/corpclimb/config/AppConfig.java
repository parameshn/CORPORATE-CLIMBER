package com.corpclimb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.base-url:https://generativelanguage.googleapis.com/v1beta}")
    private String geminiBaseUrl;

    @Value("${google.credentials.path}")
    private String googleCredentialsPath;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getGeminiApiKey() {
        return geminiApiKey;
    }

    public String getGeminiBaseUrl() {
        return geminiBaseUrl;
    }

    public String getGoogleCredentialsPath() {
        return googleCredentialsPath;
    }
}