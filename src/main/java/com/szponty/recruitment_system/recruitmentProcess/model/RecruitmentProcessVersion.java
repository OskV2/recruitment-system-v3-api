package com.szponty.recruitment_system.recruitmentProcess.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "recruitment_process_version")
public class RecruitmentProcessVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "recruitment_process_id", nullable = false)
    private RecruitmentProcess recruitmentProcess;

    private UUID version;

    @ColumnDefault("false")
    private boolean active;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
