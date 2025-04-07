package com.elyashevich.user_service.api.mapper.impl;

import com.elyashevich.user_service.api.dto.CandidateProfileDto;
import com.elyashevich.user_service.api.mapper.CandidateProfileMapper;
import com.elyashevich.user_service.api.mapper.UserMapper;
import com.elyashevich.user_service.domain.entity.CandidateProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CandidateProfileMapperImpl implements CandidateProfileMapper {

    private final UserMapper userMapper;

    @Override
    public CandidateProfileDto toDto(CandidateProfile candidateProfile) {
        return new CandidateProfileDto(
            candidateProfile.getId(),
            this.userMapper.toDto(candidateProfile.getUser()),
            candidateProfile.getExperienceLevel(),
            candidateProfile.getPreferredInterviewTypes(),
            candidateProfile.getSkills()
        );
    }

    @Override
    public CandidateProfile toEntity(CandidateProfileDto candidateProfileDto) {
        return CandidateProfile.builder()
            .id(candidateProfileDto.id())
            .user(this.userMapper.toUser(candidateProfileDto.user()))
            .experienceLevel(candidateProfileDto.experienceLevel())
            .preferredInterviewTypes(candidateProfileDto.preferredInterviewTypes())
            .skills(candidateProfileDto.skills())
            .build();
    }

    @Override
    public List<CandidateProfileDto> toDtoList(List<CandidateProfile> candidateProfiles) {
        return candidateProfiles.stream()
            .map(this::toDto)
            .toList();
    }
}
