package com.example.userauth.security;

import com.example.userauth.repository.AdminRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AdminRepository adminRepository; // 🔹 추가

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, AdminRepository adminRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.adminRepository = adminRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null) {
            if (jwtTokenProvider.isTokenValid(token)) {
                String email = jwtTokenProvider.getUsernameFromToken(token);
                System.out.println("JWT Token is valid. Email: " + email); // 로그 추가

                adminRepository.findByEmail(email).ifPresent(admin -> {
                    System.out.println("Admin found for email: " + email);
                    Authentication authentication = jwtTokenProvider.getAuthentication(admin);
                    System.out.println("Authentication object: " + authentication);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
            } else {
                System.out.println("JWT Token is invalid"); // 로그 추가
            }
        } else {
            System.out.println("JWT Token not found"); // 로그 추가
        }

            filterChain.doFilter(request, response);
        }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

