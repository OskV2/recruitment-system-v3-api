package com.szponty.recruitment_system.joboffer.model;

import com.szponty.recruitment_system.dictionary.model.*;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcessVersion;
import com.szponty.recruitment_system.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

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
    private ContractType contractType;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne
    @JoinColumn(name = "full_time_equivalent_id", nullable = false)
    private FullTimeEquivalent fullTimeEquivalent;

    @ManyToOne
    @JoinColumn(name = "work_model_id", nullable = false)
    private WorkModel workModel;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "recruitment_process_version_id", nullable = false)
    private RecruitmentProcessVersion recruitmentProcessVersion;

    @ManyToOne
    @JoinColumn(name = "recruiter_id", nullable = false)
    private User recruiterId;

    @ManyToOne
    @JoinColumn(name = "substitute_recruiter_id", nullable = false)
    private User substituteRecruiterId;

    @OneToMany(mappedBy = "jobOffer")
    private Set<JobOfferBenefit> benefits = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)  //  czacior to podpowiedzial
    @Column(name = "offer_status", nullable = false, columnDefinition = "offer_status")
    @ColumnDefault("DRAFT")
    private JobOfferStatus offerStatus;
    private int vacancy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
