package com.elyashevich.user_service.service.impl;

import com.elyashevich.user_service.domain.entity.CandidateProfile;
import com.elyashevich.user_service.domain.entity.ExperienceLevel;
import com.elyashevich.user_service.domain.entity.UserRole;
import com.elyashevich.user_service.repository.CandidateProfileRepository;
import com.elyashevich.user_service.service.CandidateProfileService;
import com.elyashevich.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandidateProfileServiceImpl implements CandidateProfileService {

    public final CandidateProfileRepository candidateProfileRepository;
    public final UserService userService;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value="CandidateProfileService::findAll")
    public Page<CandidateProfile> findAll(int page, int size) {
        log.debug("Attempting to find all profiles");

        var pageable = PageRequest.of(page, size);

        var profiles = this.candidateProfileRepository.findAll(pageable);

        log.info("Found {} profiles", profiles.getTotalElements());
        return profiles;
    }

    @Override
    @Caching(
        put = {
            @CachePut(value = "CandidateProfileService::findById", key="#result.id"),
            @CachePut(value = "CandidateProfileService::findByEmail", key = "#result.user.email"),
            @CachePut(value = "CandidateProfileService::findAll")
        }
    )
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CandidateProfile activate(Long userId) {
        var user = this.userService.findById(userId);
        user.setRole(UserRole.CANDIDATE);
        this.userService.update(userId, user);
        return this.candidateProfileRepository.save(CandidateProfile.builder()
                .experienceLevel(ExperienceLevel.LOW)
                .user(user)
            .build());
    }

    @Override
    public CandidateProfile findById(Long id) {
        return null;
    }

    @Override
    public CandidateProfile findByEmail(String email) {
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deactivate(Long userId) {
        var user = this.userService.findById(userId);
        user.setRole(UserRole.GUEST);
        this.userService.update(userId, user);
    }
}
