package com.taptalk.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutesConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Firebase는 클라이언트에서 직접 연결합니다.
                // 필요한 경우에만 백엔드 서비스로 라우팅합니다.
                
                // Eureka 서버 라우트
                .route("eureka-web-route", r -> r
                        .path("/eureka/web")
                        .filters(f -> f.setPath("/"))
                        .uri("http://localhost:8761"))
                
                // Eureka 정적 리소스 라우트
                .route("eureka-static-route", r -> r
                        .path("/eureka/**")
                        .uri("http://localhost:8761"))

                .build();
    }
}