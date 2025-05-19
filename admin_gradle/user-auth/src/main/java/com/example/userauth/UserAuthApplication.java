package com.example.userauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
// Ensure that the required Spring Cloud dependency is present in the project's build configuration (e.g., Maven or Gradle).

@SpringBootApplication
@EnableScheduling  // 스케줄링 기능 활성화
@EnableDiscoveryClient // Eureka Client 활성화
public class UserAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserAuthApplication.class, args);
    }
}
