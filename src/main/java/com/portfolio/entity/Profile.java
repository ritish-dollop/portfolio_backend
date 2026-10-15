package com.portfolio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Profile extends BaseAuditedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, length = 36)
    private String id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "professional_title", nullable = false, length = 150)
    private String professionalTitle;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @Column(name = "short_introduction", length = 500)
    private String shortIntroduction;

    @Column(name = "about_me", columnDefinition = "TEXT")
    private String aboutMe;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(length = 150)
    private String location;

    @Column(name = "resume_url", length = 500)
    private String resumeUrl;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "available_for_work", nullable = false)
    private boolean availableForWork;
}
