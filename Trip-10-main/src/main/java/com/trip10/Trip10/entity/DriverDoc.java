package com.trip10.Trip10.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "driver_doc")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DriverDoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "driver_id",nullable = false)
    private int driverId;

    @Column(name = "document_id")
    private int documentId;

    @Column(name = "file_name",nullable = false)
    private String fileName;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name = "path")
    private String path;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_status")
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Column(name = "rejection_reason")
    private String rejectionReason;

}
