package com.elyashevich.user_service.api.controller;

import com.elyashevich.user_service.api.dto.CandidateProfileDto;
import com.elyashevich.user_service.api.mapper.CandidateProfileMapper;
import com.elyashevich.user_service.service.CandidateProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;
    private final CandidateProfileMapper candidateProfileMapper;

    @GetMapping
    public List<CandidateProfileDto> findAll(
        @RequestParam(name = "min", defaultValue = "0") int page,
        @RequestParam(name = "max", defaultValue = "10") int size
    ) {
        var candidates = this.candidateProfileService.findAll(page, size);
        return this.candidateProfileMapper.toDtoList(candidates.getContent());
    }

    @GetMapping("/{id}")
    public CandidateProfileDto findById(@PathVariable("id") Long id) {
        var candidate = this.candidateProfileService.findById(id);
        return this.candidateProfileMapper.toDto(candidate);
    }

    @GetMapping("/{email}")
    public CandidateProfileDto findByEmail(@PathVariable("email") String email) {
        var candidate = this.candidateProfileService.findByEmail(email);
        return this.candidateProfileMapper.toDto(candidate);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CandidateProfileDto create(@PathVariable("id") Long id) {
        var newCandidate = this.candidateProfileService.activate(id);
        return this.candidateProfileMapper.toDto(newCandidate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.candidateProfileService.deactivate(id);
    }
}
