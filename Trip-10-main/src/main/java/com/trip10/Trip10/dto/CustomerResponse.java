package com.trip10.Trip10.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerResponse {

    private int customerId;
    private String customerName;
    private String customerEmail;
    private String phoneNumber;
    private boolean otpVerified;
    private String message;
    private String token;

    public CustomerResponse(int customerId, String customerName, String customerEmail, String phoneNumber) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.phoneNumber = phoneNumber;
    }
}
