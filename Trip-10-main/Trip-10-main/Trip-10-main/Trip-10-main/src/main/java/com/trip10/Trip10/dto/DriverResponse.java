package com.trip10.Trip10.dto;

import com.trip10.Trip10.entity.DriverDoc;
import com.trip10.Trip10.entity.DriverType;
import com.trip10.Trip10.entity.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponse {


    private int id;
    private int docId;
    private String driverName;
    private String driverEmail;
    private String driverPhone;
    private VerificationStatus verificationStatus=VerificationStatus.PENDING;
    private boolean otpVerified;
    private String message;
    private LocalDateTime createdOn;

    private DriverType driverType;






}
