package com.trip10.Trip10.service.impl;

import com.trip10.Trip10.entity.Admin;
import com.trip10.Trip10.service.DriverService;
import com.trip10.Trip10.config.AccountConflictChecker;
import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.DriverRequest;
import com.trip10.Trip10.dto.DriverResponse;
import com.trip10.Trip10.dto.UpdateUserRequest;
import com.trip10.Trip10.entity.VerificationStatus;
import com.trip10.Trip10.entity.Driver;
import com.trip10.Trip10.repos.DriverRepo;
import com.trip10.Trip10.repos.Otprepo;
import com.trip10.Trip10.service.PermissionCheckService;
import com.trip10.Trip10.service.JwtService;
import com.trip10.Trip10.service.adminservice.AdminSecurityService;
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
    private final AdminSecurityService adminSecurityService;
    private final PermissionCheckService permissionCheckService;
    private final JwtService jwtService;

    @Autowired
    public DriverServiceImpl(DriverRepo driverRepo, Otprepo otprepo, AccountConflictChecker accountConflictChecker, PasswordEncoder passwordEncoder, AdminSecurityService adminSecurityService, PermissionCheckService permissionCheckService, JwtService jwtService) {
        this.driverRepo = driverRepo;
        this.otprepo = otprepo;
        this.accountConflictChecker = accountConflictChecker;
        this.passwordEncoder = passwordEncoder;
        this.adminSecurityService = adminSecurityService;
        this.permissionCheckService = permissionCheckService;
        this.jwtService = jwtService;
    }

    private DriverResponse toResponse(Driver driver) {
        DriverResponse d = new DriverResponse();
        d.setId(driver.getId());
        d.setDriverName(driver.getName());
        d.setDriverEmail(driver.getEmail());
        d.setDriverPhone(driver.getPhoneNumber());
        d.setVerificationStatus(driver.getAuthStatus());
        d.setOtpVerified(driver.isOtpVerification());
        d.setCreatedOn(driver.getCreatedAt());
        d.setDriverType(driver.getDriverType());
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
        String phoneConflict = accountConflictChecker.phoneConflict(normalizePhone(request.getPhoneNumber()));
        if (phoneConflict != null) return ApiResponse.conflict(phoneConflict);

        String emailConflict = accountConflictChecker.emailConflict(request.getEmail());
        if (emailConflict != null) return ApiResponse.conflict(emailConflict);

        Driver driver = new Driver();
        driver.setName(request.getDriverName());
        driver.setEmail(request.getEmail());
        driver.setPassword(passwordEncoder.encode(request.getPassword()));
        driver.setPhoneNumber(normalizePhone(request.getPhoneNumber()));
        driver.setAuthStatus(VerificationStatus.PENDING);
        driver.setOtpVerification(false);
        driver.setDriverType(request.getDriverType());

        Driver saved = driverRepo.save(driver);
        DriverResponse response = toResponse(saved);
        response.setMessage("Registered, not verified yet");
        response.setToken(jwtService.generateToken(saved.getEmail()));
        return ApiResponse.created("Driver registered successfully", response);
    }

    @Override
    @Transactional
    public ApiResponse<DriverResponse> verifyDriver(int driverID) {

        Admin admin = adminSecurityService.getCurrentAdmin();

        if (!permissionCheckService.canApproveDrivers(admin)) {
            return ApiResponse.forbidden("Admin cannot approve drivers");
        }

        return driverRepo.findById(driverID)
                .map(driver -> {
                    if (driver.getAuthStatus() == VerificationStatus.VERIFIED)
                        return ApiResponse.<DriverResponse>badRequest("Driver is already verified");

                    driver.setAuthStatus(VerificationStatus.VERIFIED);
                    driverRepo.save(driver);

                    DriverResponse response = toResponse(driver);
                    response.setMessage("Driver verified successfully");
                    return ApiResponse.success("Driver verified successfully", response);
                })
                .orElse(ApiResponse.notFound("Driver not found: " + driverID));
    }
    @Override
    @Transactional
    public ApiResponse<String> sendOtp(String phoneNumber) {
        String normalizePhone = normalizePhone(phoneNumber);
        return driverRepo.findDriverByPhoneNumber(normalizePhone)
                .map(driver -> {
                    String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
                    driver.setOtpCode(otp);
                    driver.setOtpExpiresAt(LocalDateTime.now().plusMinutes(5));
                    driverRepo.save(driver);
                    return ApiResponse.<String>success("OTP generated successfully", null);
                })
                .orElse(ApiResponse.notFound("No driver found with phoneNumber: " + normalizePhone));
    }

    @Override
    @Transactional
    public ApiResponse<DriverResponse> verifyOtp(String phoneNumber, String otp) {
        phoneNumber=normalizePhone(phoneNumber);

        return driverRepo.findDriverByPhoneNumber(phoneNumber)
                .map(driver -> {
                    if (driver.getOtpCode()==null)
                        return ApiResponse.<DriverResponse>badRequest("no otp sent to this phone number");
                    if (driver.getOtpExpiresAt().isBefore(LocalDateTime.now()))
                        return ApiResponse.<DriverResponse>badRequest("otp code sent was expired");
                    if (!driver.getOtpCode().equals(otp))
                        return ApiResponse.<DriverResponse>badRequest("invalid otp code");

                    driver.setOtpVerification(true);
                    driver.setOtpCode(null);
                    driver.setOtpExpiresAt(null);
                    driverRepo.save(driver);


                    DriverResponse response=toResponse(driver);

                    return ApiResponse.success("driver phone number verified ",response);
                }).orElse(ApiResponse.notFound("no driver found with this number"+ phoneNumber));

    }

    @Override
    public ApiResponse<DriverResponse> login(DriverRequest request) {
      return driverRepo.findDriverByPhoneNumber(normalizePhone(request.getPhoneNumber()))
                .map(driver -> {
                    if (!passwordEncoder.matches(request.getPassword(), driver.getPassword()))
                        return ApiResponse.<DriverResponse>badRequest("Invalid phone number or password");

                    String message = "Login successful";

                    DriverResponse response = toResponse(driver);
                    response.setMessage(message);
                    response.setToken(jwtService.generateToken(driver.getEmail()));
                    return ApiResponse.success(message, response);
                })
                .orElse(ApiResponse.notFound("there is no account found with this phone number"));
    }

    @Override
    public ApiResponse<DriverResponse> update(int id, UpdateUserRequest request) {
        Driver driver = driverRepo.findById(id).orElse(null);
        if (driver == null) return ApiResponse.notFound("driver not found: " + id);

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
            driver.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            driver.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        driverRepo.save(driver);
        return ApiResponse.success("driver updated successfully", toResponse(driver));
    }

//
//    @Override
//    public ApiResponse<DriverResponse> updateDoc(int id, DriverRequest request) {
//        Driver driver = driverRepo.findById(id).orElse(null);
//        if (driver==null) return ApiResponse.notFound("driver not found: " + id);
//        driver.setDocs(request.getDocs());
//
//        driverRepo.save(driver);
//        return ApiResponse.success("docs updated successfully", toResponse(driver));
//    }

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