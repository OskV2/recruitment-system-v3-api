package com.szponty.recruitment_system.joboffer.model;

import com.szponty.recruitment_system.dictionary.model.Benefit;
import com.szponty.recruitment_system.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "job_offer")
public class JobOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String description;

    private int salaryFrom;
    private int salaryTo;
    private String currency;

    private String[] mustHaveRequirements;
    private String[] niceToHaveRequirements;

    private LocalDateTime validFrom;
    private LocalDateTime validTo;

    @ManyToOne
    @JoinColumn(name = "contract_type_id", nullable = false)
    private UUID contractTypeId;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private UUID locationId;

    @ManyToOne
    @JoinColumn(name = "full_time_equivalent_id", nullable = false)
    private UUID fullTimeEquivalentId;

    @ManyToOne
    @JoinColumn(name = "work_model_id", nullable = false)
    private UUID workModelId;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private UUID departmentId;

    @OneToOne
    @JoinColumn(name = "recruiter_id", nullable = false)
    private User recruiterId;

    @OneToOne
    @JoinColumn(name = "substitute_recruiter_id", nullable = false)
    private User substituteRecruiterId;

    @OneToMany(mappedBy = "benefit")
    private Set<JobOfferBenefit> benefits = new HashSet<>();

    @ColumnDefault("DRAFT")
    private JobOfferStatus offerStatus;
    private int vacancy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
