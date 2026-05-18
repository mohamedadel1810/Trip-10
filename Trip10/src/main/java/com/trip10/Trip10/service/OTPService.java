package com.trip10.Trip10.service;

import com.trip10.Trip10.entity.OTPVerification;
import com.trip10.Trip10.repos.Otprepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OTPService {

    private static Otprepo otprepo;

    public OTPService(Otprepo otprepo) {OTPService.otprepo = otprepo;}

    public static boolean verifyOtp(String phoneNumber, String otp) {

        OTPVerification record =otprepo
                .findByPhoneNumber(phoneNumber)
                .orElse(null);

        if (record == null) return false;
        if (record.getExpiresAt().isBefore(LocalDateTime.now()))return false;
        if (!record.getOtpCode().equals(otp))return false;

        record.setIsVerified(true);
        otprepo.save(record);
        return true;
    }
}
