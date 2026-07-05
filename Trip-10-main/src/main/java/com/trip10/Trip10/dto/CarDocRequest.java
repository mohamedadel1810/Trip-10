package com.trip10.Trip10.dto;

import com.trip10.Trip10.entity.VerificationStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CarDocRequest {

    @NotBlank(message = "doc required")
    private int carId;
    /// /////////////////////////
    private int documentId;
/// ////////////////////////////////
    @NotBlank(message = "file name required")
    private String fileName;

    @NotBlank
    private String path;

    private LocalDateTime uploadedAt;

    private VerificationStatus verificationStatus;


    private String rejectionReason;
}
