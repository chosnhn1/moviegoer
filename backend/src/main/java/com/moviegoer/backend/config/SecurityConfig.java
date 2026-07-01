package com.moviegoer.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.moviegoer.backend.security.JwtAuthenticationEntryPoint;
import com.moviegoer.backend.security.JwtRequestFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private JwtRequestFilter jwtRequestFilter;
    
    @Autowired
    public SecurityConfig(
        JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
        JwtRequestFilter jwtRequestFilter
    ) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(
        AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(requests ->
                requests.requestMatchers(
                    "/api/v1/auth/login",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                )
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/accounts")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/user/{userId}")
                .permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/v1/user/{userId}")
                .hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/article")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "api/v1/article/**")
                .hasRole("AUTHOR")
                .requestMatchers(HttpMethod.PATCH, "api/v1/article/**")
                .hasRole("AUTHOR")
                .requestMatchers(HttpMethod.DELETE, "api/v1/article/**")
                .hasRole("AUTHOR")
                .anyRequest()
                .authenticated()
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(
                jwtAuthenticationEntryPoint
            ))
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
            ));

        
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }



}
