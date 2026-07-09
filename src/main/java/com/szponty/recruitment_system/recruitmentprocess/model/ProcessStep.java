package com.szponty.recruitment_system.recruitmentprocess.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "process_step")
public class ProcessStep {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "process_version_id")
    private RecruitmentProcessVersion processVersion;

    private String name;
    private String description;

    @ColumnDefault("false")
    private boolean requiresInterview;
    @ColumnDefault("false")
    private boolean requiresDepartmentApproval;

    @ColumnDefault("false")
    private boolean deleted;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
