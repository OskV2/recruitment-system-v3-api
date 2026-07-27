package com.szponty.recruitment_system.jobApplication.model;

import com.szponty.recruitment_system.joboffer.model.JobOffer;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcessVersion;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "job_application")
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private UUID publicToken;

    @ManyToOne
    @JoinColumn(name = "job_offer_id")
    private JobOffer jobOffer;

    @ManyToOne
    @JoinColumn(name = "recruitment_process_version_id")
    private RecruitmentProcessVersion recruitmentProcessVersion;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    @Column(name = "status", nullable = false, columnDefinition = "job_application_status")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private JobApplicationStatus status;

    @Nullable
    private String githubLink;

    @ColumnDefault("false")
    private boolean deleted;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
