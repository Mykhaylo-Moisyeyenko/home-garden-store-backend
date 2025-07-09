package com.homegarden.store.backend.model.dto;

import com.homegarden.store.backend.dto.FavoriteDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validFavoriteDto_shouldPassValidation() {
        FavoriteDto dto = new FavoriteDto(1L, 100L);
        Set<ConstraintViolation<FavoriteDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void nullUserId_shouldFailValidation() {
        FavoriteDto dto = new FavoriteDto(null, 100L);
        Set<ConstraintViolation<FavoriteDto>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("userId"));
    }

    @Test
    void negativeProductId_shouldFailValidation() {
        FavoriteDto dto = new FavoriteDto(1L, -1L);
        Set<ConstraintViolation<FavoriteDto>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("productId"));
    }
}