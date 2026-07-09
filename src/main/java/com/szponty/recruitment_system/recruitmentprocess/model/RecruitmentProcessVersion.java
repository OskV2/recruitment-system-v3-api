package com.szponty.recruitment_system.recruitmentprocess.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(
            mappedBy = "processVersion",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<ProcessStep> steps = new ArrayList<>();

    @ColumnDefault("false")
    private boolean active;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
