package com.homegarden.store.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth

//                        .requestMatchers(    "/swagger-ui.html",
//                                "/swagger-ui/**",
//                                "/v3/api-docs/**",
//                                "/swagger-resources/**",
//                                "/webjars/**").permitAll()
//
//                        .requestMatchers("/login").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/v1/products/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/v1/categories/**").permitAll()
//
//                        .requestMatchers(HttpMethod.POST, "/v1/users/register").anonymous()
//
//                        .requestMatchers(HttpMethod.POST,"/v1/carts").hasRole("USER")
//                        .requestMatchers(HttpMethod.GET,"/v1/carts/{id}").hasRole("USER")
//                        .requestMatchers(HttpMethod.DELETE,"/v1/carts/{id}").hasRole("USER")
//                        .requestMatchers(HttpMethod.POST, "/v1/cart-items").hasRole("USER")
//                        .requestMatchers(HttpMethod.PUT, "/v1/cart-items/{id}").hasRole("USER")
//                        .requestMatchers(HttpMethod.GET,"/v1/cart-items/{id}").hasRole("USER")
//                        .requestMatchers(HttpMethod.DELETE,"/v1/cart-items/{id}").hasRole("USER")
//                        .requestMatchers("v1/favorites/**").hasRole("USER")
//                        .requestMatchers(HttpMethod.POST, "/v1/orders").hasRole("USER")
//                        .requestMatchers(HttpMethod.GET, "/v1/orders/{orderId}").hasRole("USER")
//                        .requestMatchers(HttpMethod.GET, "/v1/orders/history/{userId}").hasRole("USER")
//                        .requestMatchers(HttpMethod.PATCH, "/v1/orders/{orderId}/cancel").hasRole("USER")
//                        .requestMatchers(HttpMethod.POST, "/v1/payments").hasRole("USER")
//                        .requestMatchers(HttpMethod.GET, "/v1/payments/{paymentId}").hasRole("USER")
//                        .requestMatchers(HttpMethod.GET, "/v1/payments/payments-by-order/{orderId}").hasRole("USER")
//                        .requestMatchers(HttpMethod.PUT, "v1/users/{userId}").hasRole("USER")
//                        .requestMatchers(HttpMethod.GET, "/v1/users/id/{id}").hasRole("USER")
//
//                        .requestMatchers("/**").hasRole("ADMINISTRATOR")

                        .anyRequest().permitAll()

                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}