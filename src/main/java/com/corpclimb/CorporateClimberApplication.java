package com.corpclimb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class CorporateClimberApplication {
    public static void main(String[] args) {
        SpringApplication.run(CorporateClimberApplication.class, args);
    }
}