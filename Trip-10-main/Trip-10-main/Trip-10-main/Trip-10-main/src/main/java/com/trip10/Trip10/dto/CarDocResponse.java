package com.trip10.Trip10.dto;

import com.trip10.Trip10.entity.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarDocResponse {

    private int carDocId;

    private int carId;
    /// /////////////////////
    private int documentId;
/// ///////////////////////////
    private String fileName;

    private String path;

    private LocalDateTime uploadedAt;

    private VerificationStatus verificationStatus;

    private String rejectionReason;
}
