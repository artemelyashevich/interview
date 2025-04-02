package com.elyashevich.user_service.repository;

import com.elyashevich.user_service.domain.entity.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long> {
}
