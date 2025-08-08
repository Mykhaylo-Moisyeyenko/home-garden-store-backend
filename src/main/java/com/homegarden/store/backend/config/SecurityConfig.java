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

                        .requestMatchers(HttpMethod.POST, "/v1/carts").hasAnyRole("USER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET,"/v1/carts/{id}").hasAnyRole("USER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE,"/v1/carts/{id}").hasAnyRole("USER", "ADMINISTRATOR")
                                       
                        .requestMatchers(HttpMethod.POST, "/v1/cart-items").hasAnyRole("USER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT, "/v1/cart-items/{id}").hasAnyRole("USER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET,"/v1/cart-items/{id}").hasAnyRole("USER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE,"/v1/cart-items/{id}").hasAnyRole("USER", "ADMINISTRATOR")
                                       
                        .requestMatchers("/v1/favorites/**").hasAnyRole("USER", "ADMINISTRATOR")
                                       
                        .requestMatchers(HttpMethod.POST, "/v1/orders").hasAnyRole("USER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/v1/orders/{orderId}").hasAnyRole("USER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/v1/orders/history/{userId}").hasAnyRole("USER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/v1/orders/{orderId}/cancel").hasAnyRole("USER", "ADMINISTRATOR")
                                       
                        .requestMatchers(HttpMethod.POST, "/v1/payments").hasAnyRole("USER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/v1/payments/{paymentId}").hasAnyRole("USER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/v1/payments/payments-by-order/{orderId}").hasAnyRole("USER", "ADMINISTRATOR")
                                       
                        .requestMatchers(HttpMethod.PUT, "/v1/users/{userId}").hasAnyRole("USER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/v1/users/id/{id}").hasAnyRole("USER", "ADMINISTRATOR")

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