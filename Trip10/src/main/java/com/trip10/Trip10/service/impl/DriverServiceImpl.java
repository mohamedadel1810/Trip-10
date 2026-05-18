package com.trip10.Trip10.service.impl;

import com.trip10.Trip10.service.DriverService;
import com.trip10.Trip10.config.AccountConflictChecker;
import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.DriverRequest;
import com.trip10.Trip10.dto.DriverResponse;
import com.trip10.Trip10.dto.UpdateUserRequest;
import com.trip10.Trip10.entity.AuthStatus;
import com.trip10.Trip10.entity.Driver;
import com.trip10.Trip10.repos.DriverRepo;
import com.trip10.Trip10.repos.Otprepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverServiceImpl implements DriverService {
    private final DriverRepo driverRepo;
    private final Otprepo otprepo;
    private final AccountConflictChecker accountConflictChecker;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DriverServiceImpl(DriverRepo driverRepo, Otprepo otprepo, AccountConflictChecker accountConflictChecker, PasswordEncoder passwordEncoder) {
        this.driverRepo = driverRepo;
        this.otprepo = otprepo;
        this.accountConflictChecker = accountConflictChecker;
        this.passwordEncoder = passwordEncoder;
    }

    private DriverResponse toResponse(Driver driver) {
        DriverResponse d = new DriverResponse();
        d.setId(driver.getId());
        d.setDriverName(driver.getName());
        d.setDriverEmail(driver.getEmail());
        d.setDriverPhone(driver.getPhone_number());
        d.setAuthStatus(driver.getAuth_status());
        d.setOtpVerified(driver.isOtp_verification());
        d.setCreatedOn(driver.getCreatedAt());
        return d;
    }

    private String normalizePhone(String phone) {
        if (phone == null) return null;
        if (phone.startsWith(" ")) phone = "+" + phone.substring(1);
        return phone.trim();
    }

    @Override
    public List<DriverResponse> findAll() {
        List<Driver> drivers = (List<Driver>) driverRepo.findAll();
       return drivers.stream().map(this::toResponse)
                .collect(Collectors.toList());

    }

    @Override
    public ApiResponse<DriverResponse> findById(int id) {
        return driverRepo.findById(id)
                .map(driver -> ApiResponse.success("Driver fetched successfully", toResponse(driver)))
                .orElse(ApiResponse.notFound("Driver not found: " + id));
    }

    @Override
    @Transactional
    public ApiResponse<DriverResponse> create(DriverRequest request) {
        String emailConflict = accountConflictChecker.emailConflict(request.getEmail());
        if (emailConflict != null) return ApiResponse.conflict(emailConflict);

        String phoneConflict = accountConflictChecker.phoneConflict(normalizePhone(request.getPhoneNumber()));
        if (phoneConflict != null) return ApiResponse.conflict(phoneConflict);

        Driver driver = new Driver();
        driver.setName(request.getDriverName());
        driver.setEmail(request.getEmail());
        driver.setPassword(passwordEncoder.encode(request.getPassword()));
        driver.setPhone_number(normalizePhone(request.getPhoneNumber()));
        driver.setAuth_status(AuthStatus.PENDING);
        driver.setOtp_verification(false);

        Driver saved = driverRepo.save(driver);
        DriverResponse response = toResponse(saved);
        response.setMessage("Registered, not verified yet");
        return ApiResponse.created("Driver registered successfully", response);
    }

    @Override
    @Transactional
    public ApiResponse<DriverResponse> verifyDriver(int driverID, String r) {
        return driverRepo.findById(driverID)
                .map(driver -> {
                    if (driver.getAuth_status() == AuthStatus.AUTHORIZED)
                        return ApiResponse.<DriverResponse>badRequest("Driver is already verified");

                    driver.setAuth_status(AuthStatus.AUTHORIZED);
                    driverRepo.save(driver);

                    DriverResponse response = toResponse(driver);
                    response.setMessage("Driver verified successfully");
                    return ApiResponse.success("Driver verified successfully", response);
                })
                .orElse(ApiResponse.notFound("Driver not found: " + driverID));
    }
    @Override
    @Transactional
    public ApiResponse<Void> sendOtp(String phoneNumber) {
        String normalizePhone = normalizePhone(phoneNumber);
        return driverRepo.findDriverPhoneNumber(normalizePhone)
                .map(driver -> {
                    String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
                    driver.setOtp_code(otp);
                    driver.setOtpExpiresAt(LocalDateTime.now().plusMinutes(5));
                    driverRepo.save(driver);
                    return ApiResponse.<Void>success("OTP generated successfully", null);
                })
                .orElse(ApiResponse.notFound("No driver found with phoneNumber: " + normalizePhone));
    }

    @Override
    public ApiResponse<DriverResponse> login(DriverRequest request) {
      return driverRepo.findDriverPhoneNumber(normalizePhone(request.getPhoneNumber()))
                .map(driver -> {
                    if (!passwordEncoder.matches(request.getPassword(), driver.getPassword()))
                        return ApiResponse.<DriverResponse>badRequest("Invalid phone number or password");

                    String message = "Login successful";

                    DriverResponse response = toResponse(driver);
                    response.setMessage(message);
                    return ApiResponse.success(message, response);
                })
                .orElse(ApiResponse.notFound("there is no account found with this phone number"));
    }

    @Override
    public ApiResponse<DriverResponse> update(int id, UpdateUserRequest request) {
        Driver driver = driverRepo.findById(id).orElse(null);
        if (driver == null) return ApiResponse.notFound("User not found: " + id);

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            driver.setName(request.getUsername());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            boolean emailTaken = driverRepo.findDriverByEmail(request.getEmail())
                    .filter(other -> other.getId() != id)
                    .isPresent();
            if (emailTaken) return ApiResponse.conflict("Email is already taken");
            driver.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
            driver.setPhone_number(request.getPhoneNumber());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            driver.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        driverRepo.save(driver);
        return ApiResponse.success("User updated successfully", toResponse(driver));
    }
    @Override
    @Transactional
    public ApiResponse<Void> deleteById(int id) {
        return driverRepo.findById(id)
                .map(driver -> {
                    driver.setDeletedAt(java.time.LocalDateTime.now());
                    driverRepo.save(driver);
                    return ApiResponse.<Void>success("Driver deleted successfully", null);
                })
                .orElse(ApiResponse.notFound("Driver not found: " + id));
    }
}