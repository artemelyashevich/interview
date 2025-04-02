package com.elyashevich.user_service.domain.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "candidate_profile")
@Getter
@Setter
@Builder
@ToString(exclude = "user")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 50)
    private String experienceLevel;

    @Column(name = "preferred_interview_types", length = 255)
    private String preferredInterviewTypes;

    @ElementCollection
    @CollectionTable(
        name = "candidate_skills",
        joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "skill", nullable = false, length = 100)
    private Set<String> skills;
}