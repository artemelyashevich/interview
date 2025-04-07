package com.elyashevich.user_service.api.mapper;

import com.elyashevich.user_service.api.dto.CandidateProfileDto;
import com.elyashevich.user_service.domain.entity.CandidateProfile;

import java.util.List;

public interface CandidateProfileMapper {

    CandidateProfileDto toDto(CandidateProfile candidateProfile);

    CandidateProfile toEntity(CandidateProfileDto candidateProfileDto);

    List<CandidateProfileDto> toDtoList(List<CandidateProfile> candidateProfiles);
}
