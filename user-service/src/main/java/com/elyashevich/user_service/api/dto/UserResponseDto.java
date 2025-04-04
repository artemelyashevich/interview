package com.elyashevich.user_service.api.dto;

import com.elyashevich.user_service.domain.entity.UserRole;

public record UserResponseDto(
    Long id,
    String email,
    UserRole role
) {
}
