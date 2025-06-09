package com.example.userauth.config;

import com.example.userauth.security.JwtAuthenticationFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import com.example.userauth.security.JwtTokenProvider;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults; // 👉 import 추가

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter; // 🔹 필터 주입

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (API 호출 시 필요)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 명시적 CORS 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // OPTIONS 요청 허용
                        .requestMatchers("/api/auth/register","/api/auth/send-verification-code",
                                "/api/auth/verify", "/api/auth/login", "/api/auth/invite-code/generate", "/api/auth/invite-code/validate",
                                "/api/users/{user_id}","/api/users/","/api/users", "/api/users/**",
                                "/api/reports/users","/api/reports/posts",
                                "/api/reports/{report_id}","/api/reports/{reportId}/status"
                                ,"/api/reports/chats", "api/auth/admins","api/auth/admins/{id}",
                                "/api/posts/**","/api/users/{user_id}/status", "/api/posts/{post_id}",
                                "api/users/withdrawal-reasons","/api/reports/{report_id}/process",
                                "/api/hashtags","/api/hashtags/{hashtag_id}","/api/hashtags/wordcloud",
                                "/api/users/statistics", "/api/posts/statistics", "/api/hashtags/{hashtag_id}/status",
                                "/api/hashtags/statistics","/api/admins/{admin_id}/role", "/api/auth/generate-token",
                                "/api/auth/admins/","/api/users/statistics","/api/admins/inquiries","/api/chats/rooms/{roomId}",
                                "/api/dashboard/**").permitAll()
                        .anyRequest().authenticated()
                );

        // CORS 필터를 가장 먼저 추가 (모든 다른 필터보다 앞에)
        http.addFilterBefore(corsFilter(), org.springframework.security.web.access.channel.ChannelProcessingFilter.class);
        
        // JwtAuthenticationFilter를 SecurityFilterChain에 추가
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        
        // CORS 설정을 더 포용적으로 변경
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://211.187.162.65:3000"); 
        configuration.addAllowedOrigin("http://211.187.162.65:3000/");
        
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("OPTIONS");
        configuration.addAllowedMethod("PATCH");
        configuration.addAllowedMethod("HEAD");
        
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Content-Type");
        configuration.addExposedHeader("Accept");
        configuration.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://211.187.162.65:3000"); 
        configuration.addAllowedOrigin("http://211.187.162.65:3000/");
        
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("Authorization");
        configuration.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}