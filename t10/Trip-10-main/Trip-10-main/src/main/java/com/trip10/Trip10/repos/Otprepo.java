package com.trip10.Trip10.repos;

import com.trip10.Trip10.entity.OTPVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Otprepo extends JpaRepository<OTPVerification, Integer> {
    @Override
    Optional<OTPVerification> findByPhoneNumber(String phoneNumber);
}
