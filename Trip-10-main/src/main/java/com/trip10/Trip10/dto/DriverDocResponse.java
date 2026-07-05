package com.trip10.Trip10.dto;

import com.trip10.Trip10.entity.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DriverDocResponse {

    private int id;

    private int driverId;

    private String fileName;

    private String path;

    private int documentId;

    private LocalDateTime uploadedAt;

    private VerificationStatus verificationStatus=VerificationStatus.PENDING;

    private String rejectionReason;
}
