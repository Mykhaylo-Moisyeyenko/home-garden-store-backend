package com.homegarden.store.backend.config;

import com.homegarden.store.backend.service.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**")

                        .permitAll()

                        .requestMatchers("/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/categories/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/users/register").anonymous()

                        .requestMatchers(HttpMethod.POST, "/v1/carts").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/v1/carts/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/carts/{id}").hasRole("USER")

                        .requestMatchers(HttpMethod.POST, "/v1/cart-items").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/v1/cart-items/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/v1/cart-items/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/cart-items/{id}").hasRole("USER")

                        .requestMatchers("/v1/favorites/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/v1/orders").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/v1/orders/{orderId}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/v1/orders/history/{userId}").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/v1/orders/{orderId}/cancel").hasRole("USER")

                        .requestMatchers(HttpMethod.POST, "/v1/payments").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/v1/payments/{paymentId}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/v1/payments/payments-by-order/{orderId}").hasRole("USER")

                        .requestMatchers(HttpMethod.PUT, "/v1/users/{userId}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/v1/users/id/{id}").hasRole("USER")

                        .requestMatchers("/**").hasRole("ADMINISTRATOR"))

                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}