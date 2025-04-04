package com.elyashevich.user_service.service;

import com.elyashevich.user_service.domain.entity.CandidateProfile;
import org.springframework.data.domain.Page;

public interface CandidateProfileService {

    Page<CandidateProfile> findAll(int page, int size);

    CandidateProfile activate(Long userId);

    CandidateProfile findById(Long id);

    CandidateProfile findByEmail(String email);

    void deactivate(Long userId);
}
