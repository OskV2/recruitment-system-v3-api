package com.szponty.recruitment_system.role.model;

import com.szponty.recruitment_system.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String description;
    private boolean canManageJobApplications;

    private boolean canAddNewOffer;
    private boolean canEditExistingOffer;
    private boolean canViewAllOffers;

    private boolean canManageUsers;
    private boolean canManageRoles;

    private boolean canViewLogs;

    @ColumnDefault("false")
    private boolean deleted;

    @OneToMany(mappedBy = "role")
    private List<User> users = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
