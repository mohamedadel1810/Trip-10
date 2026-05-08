package com.trip10.Trip10.config;

import com.trip10.Trip10.repos.CustomerRepo;
import com.trip10.Trip10.repos.DriverRepo;
import org.springframework.stereotype.Component;

@Component
public class AccountConflictChecker {


    private final DriverRepo driversRepo;
    private final CustomerRepo customerRepo;

    public AccountConflictChecker(DriverRepo driversRepo, CustomerRepo customerRepo) {
        this.driversRepo = driversRepo;
        this.customerRepo = customerRepo;
    }

    public String emailConflict(String email) {
        if (driversRepo.findDriverByEmail(email).isPresent())
            return "Email is already registered as a driver account";
        if (customerRepo.findCustomerByEmail(email).isPresent())
            return "Email is already registered as a customer account";
        return null;
    }

    public String phoneConflict(String phone) {
        if (driversRepo.findDriverPhoneNumber(phone).isPresent())
            return "Phone number is already registered as a driver account";
        if (customerRepo.findCustomerByPhoneNumber(phone).isPresent())
            return "Phone number is already registered as a customer account";
        return null;
    }
}
