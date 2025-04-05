package com.elyashevich.user_service.repository;

import com.elyashevich.user_service.domain.entity.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long> {

    Optional<CandidateProfile> findByUserEmail(String email);
}
