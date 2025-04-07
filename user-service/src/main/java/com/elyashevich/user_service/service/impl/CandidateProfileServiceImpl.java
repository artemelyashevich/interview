package com.elyashevich.user_service.service.impl;

import com.elyashevich.user_service.domain.entity.CandidateProfile;
import com.elyashevich.user_service.domain.entity.ExperienceLevel;
import com.elyashevich.user_service.domain.entity.UserRole;
import com.elyashevich.user_service.exception.ResourceNotFoundException;
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

    public static final String CANDIDATE_WITH_ID_WAS_NOT_FOUND_TEMPLATE = "Candidate with id '%s' was not found";
    public static final String CANDIDATE_WITH_EMAIL_WAS_NOT_FOUND_TEMPLATE = "Candidate with email '%s' was not found";

    public final CandidateProfileRepository candidateProfileRepository;
    public final UserService userService;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "CandidateProfileService::findAll")
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
            @CachePut(value = "CandidateProfileService::findById", key = "#result.id"),
            @CachePut(value = "CandidateProfileService::findByEmail", key = "#result.user.email"),
            @CachePut(value = "CandidateProfileService::findAll")
        }
    )
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CandidateProfile activate(Long userId) {
        log.debug("Attempting activate candidate with id {}", userId);

        var user = this.userService.findById(userId);
        user.setRole(UserRole.CANDIDATE);
        this.userService.update(userId, user);
        var candidate = this.candidateProfileRepository.save(CandidateProfile.builder()
            .experienceLevel(ExperienceLevel.LOW)
            .user(user)
            .build());

        log.info("User with id '{}' has been activated", userId);
        return candidate;
    }

    @Override
    public CandidateProfile findById(Long id) {
        log.debug("Attempting to find profile with id {}", id);

        var candidateProfile = this.candidateProfileRepository.findById(id).orElseThrow(
            () -> {
                var message = CANDIDATE_WITH_ID_WAS_NOT_FOUND_TEMPLATE.formatted(id);
                log.warn(message);
                return new ResourceNotFoundException(message);
            }
        );

        log.info("Candidate with id '{}' found", id);
        return candidateProfile;
    }

    @Override
    public CandidateProfile findByEmail(String email) {
        log.debug("Attempting to find profile with email {}", email);

        var candidateProfile = this.candidateProfileRepository.findByUserEmail(email).orElseThrow(
            () -> {
                var message = CANDIDATE_WITH_EMAIL_WAS_NOT_FOUND_TEMPLATE.formatted(email);
                log.warn(message);
                return new ResourceNotFoundException(message);
            }
        );

        log.info("Candidate with email '{}' found", email);
        return candidateProfile;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deactivate(Long userId) {
        log.debug("Attempting deactivate candidate with id {}", userId);

        var user = this.userService.findById(userId);
        user.setRole(UserRole.GUEST);
        this.userService.update(userId, user);

        log.info("User with id '{}' has been deactivated", userId);
    }
}
