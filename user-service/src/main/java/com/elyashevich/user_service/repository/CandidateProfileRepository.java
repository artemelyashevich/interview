package com.elyashevich.user_service.repository;

import com.elyashevich.user_service.domain.entity.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long> {

    @Query(nativeQuery = true, value = """
        SELECT 
            c.id, c.experience_level, c.preferred_interview_types, u.email, u.password, u.role
        FROM candidate_profile c
        JOIN users u ON u.email = :email;
       """)
    Optional<CandidateProfile> findEmail(@Param("email") String email);
}
