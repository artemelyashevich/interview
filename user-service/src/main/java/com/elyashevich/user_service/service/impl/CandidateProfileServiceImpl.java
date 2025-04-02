package com.elyashevich.user_service.service.impl;

import com.elyashevich.user_service.repository.CandidateProfileRepository;
import com.elyashevich.user_service.service.CandidateProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateProfileServiceImpl implements CandidateProfileService {

    public final CandidateProfileRepository candidateProfileRepository;
}
