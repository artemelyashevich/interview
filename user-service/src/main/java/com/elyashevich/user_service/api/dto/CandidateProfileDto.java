package com.elyashevich.user_service.api.dto;

import com.elyashevich.user_service.domain.entity.ExperienceLevel;

import java.util.Set;

public record CandidateProfileDto(
    Long id,
    UserResponseDto user,
    ExperienceLevel experienceLevel,
    String preferredInterviewTypes,
    Set<String> skills
) {
}
