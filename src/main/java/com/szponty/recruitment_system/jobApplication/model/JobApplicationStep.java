package com.szponty.recruitment_system.jobApplication.model;

import com.szponty.recruitment_system.recruitmentProcess.model.ProcessStep;
import com.szponty.recruitment_system.user.model.User;
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
@Table(name = "job_application_step")
public class JobApplicationStep {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private JobApplication jobApplication;

    @ManyToOne
    @JoinColumn(name = "process_step_id")
    private ProcessStep processStep;

    private Integer stepOrder;

    @Column(name = "status", nullable = false, columnDefinition = "application_step_status")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private JobApplicationStepStatus status;

    @Nullable
    private LocalDateTime startedAt;

    @ManyToOne
    @JoinColumn(name = "started_by_user_id")
    private User startedByUser;

    @Nullable
    private LocalDateTime completedAt;

    @ManyToOne
    @JoinColumn(name = "completed_by_user_id")
    private User completedByUser;

    @Nullable
    private LocalDateTime rejectedAt;

    @ManyToOne
    @JoinColumn(name = "rejected_by_user_id")
    private User rejectedByUser;

    @Nullable
    private String decisionComment;

    @Nullable
    private String rejectionReason;

    @ColumnDefault("false")
    private boolean deleted;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
