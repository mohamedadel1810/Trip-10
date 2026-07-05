package com.trip10.Trip10.dto;

import com.trip10.Trip10.entity.OwnerType;
import com.trip10.Trip10.entity.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarResponse {


    private int carId;

    private int docId;

    private  String color;

    private String brand;

    private String model;

    private String plateNumber;

    private VerificationStatus verificationStatus=VerificationStatus.PENDING;







}
