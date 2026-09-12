package com.szponty.recruitment_system.attachment.model;

import com.szponty.recruitment_system.jobApplication.model.JobApplication;
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
@Table(name = "attachment")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String originalName;

    private String storedName;

    private String path;

    @Column(name = "isCv")
    boolean cv;

    private String contentType;

    private Long sizeBytes;

    private String checksumSha256;

    @Column(name = "status", nullable = false, columnDefinition = "attachment_status")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private AttachmentStatus status;

    @ManyToOne
    @JoinColumn(name = "job_application_id")
    private JobApplication jobApplication;

    private String extractedText;

    @ColumnDefault("false")
    private boolean deleted;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
