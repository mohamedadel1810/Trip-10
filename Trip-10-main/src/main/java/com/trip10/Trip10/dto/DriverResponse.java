package com.trip10.Trip10.dto;

import com.trip10.Trip10.entity.VerificationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class DriverResponse {


    private int id;
    private String driverName;
    private String driverEmail;
    private String driverPhone;
    private VerificationStatus verificationStatus;
    private boolean otpVerified;
    private String message;
    private LocalDateTime createdOn;

}
