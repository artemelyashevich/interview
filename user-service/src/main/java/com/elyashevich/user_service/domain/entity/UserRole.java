package com.elyashevich.user_service.domain.entity;

public enum UserRole {
    CANDIDATE("Candidate"),
    INTERVIEWER("Interviewer"),
    ADMIN("Admin"),
    GUEST("Guest");

    UserRole(String role) {
    }
}