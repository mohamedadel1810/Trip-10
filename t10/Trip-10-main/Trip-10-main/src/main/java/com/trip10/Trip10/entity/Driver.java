package com.trip10.Trip10.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "driver")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "driverName", nullable = false)
    private String name;
    @Column(name = "email",nullable = false,unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "phone_number", nullable = false,unique = true)
    private String phone_number;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_status")
    private VerificationStatus auth_status= VerificationStatus.PENDING;

    @Column(name = "otp_verification")
    private boolean otp_verification;

    @Column(name = "otp_code")
    private String otp_code;

    @Column(name = "otp_expires_at")
    private LocalDateTime otpExpiresAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "driver_type")
    private DriverType driverType;

    @OneToMany(mappedBy = "Driver")
    private List<DriverDoc> docs=new ArrayList<>();




}

