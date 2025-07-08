package com.homegarden.store.backend.model.dto;

public record UserProfileDto(
        Long profileId,
        String address,
        String city,
        String country)
{}

