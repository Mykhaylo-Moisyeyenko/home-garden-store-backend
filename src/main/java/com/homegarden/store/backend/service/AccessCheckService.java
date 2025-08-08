package com.homegarden.store.backend.service;

import com.homegarden.store.backend.entity.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AccessCheckService {

    public void checkAccess(Object entity) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRATOR"))) {
            return;
        }

        String emailCurrentContextUser = auth.getName();

        if (entity instanceof CartItem cartItem) {
            if (!cartItem.getCart().getUser().getEmail().equals(emailCurrentContextUser)) {
                throw new AccessDeniedException("You are not allowed to access this resource");
            }
        } else if (entity instanceof Cart cart) {
            if (!cart.getUser().getEmail().equals(emailCurrentContextUser)) {
                throw new AccessDeniedException("You are not allowed to access this resource");
            }
        } else if (entity instanceof Favorite favorite) {
            if (!favorite.getUser().getEmail().equals(emailCurrentContextUser)) {
                throw new AccessDeniedException("You are not allowed to access this resource");
            }
        } else if (entity instanceof OrderItem orderItem) {
            if (!orderItem.getOrder().getUser().getEmail().equals(emailCurrentContextUser)) {
                throw new AccessDeniedException("You are not allowed to access this resource");
            }
        } else if (entity instanceof Order order) {
            if (!order.getUser().getEmail().equals(emailCurrentContextUser)) {
                throw new AccessDeniedException("You are not allowed to access this resource");
            }
        } else if (entity instanceof Payment payment) {
            if (!payment.getOrder().getUser().getEmail().equals(emailCurrentContextUser)) {
                throw new AccessDeniedException("You are not allowed to access this resource");
            }
        } else if (entity instanceof User userForCheck) {
            if (!userForCheck.getEmail().equals(emailCurrentContextUser)) {
                throw new AccessDeniedException("You are not allowed to access this resource");
            }
        }
    }
}