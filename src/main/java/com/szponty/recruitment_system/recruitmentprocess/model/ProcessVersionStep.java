package com.szponty.recruitment_system.recruitmentprocess.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(
        name = "process_version_step",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_process_version_step",
                        columnNames = {"recruitment_process_version_id", "process_step_id"}
                ),
                @UniqueConstraint(
                        name = "uq_process_version_step_order",
                        columnNames = {"recruitment_process_version_id", "step_order"}
                )
        }
)
public class ProcessVersionStep {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_process_version_id", nullable = false)
    private RecruitmentProcessVersion recruitmentProcessVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_step_id", nullable = false)
    private ProcessStep processStep;

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}