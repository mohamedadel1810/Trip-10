package com.trip10.Trip10.service.impl;

import com.trip10.Trip10.service.CustomerService;
import com.trip10.Trip10.service.JwtService;
import com.trip10.Trip10.dto.*;
import com.trip10.Trip10.entity.Customer;
import com.trip10.Trip10.repos.CustomerRepo;
import com.trip10.Trip10.util.OtpUtil;
import com.trip10.Trip10.util.PhoneUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepo customerRepo;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Autowired
    public CustomerServiceImpl(CustomerRepo customerRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.customerRepo = customerRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    private CustomerResponse toCustomerResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse(customer.getId(), customer.getUserName(), customer.getEmail(), customer.getPhoneNumber());
        response.setOtpVerified(customer.isOtpVerified());
        return response;
    }

    @Override
    public List<CustomerResponse> findAll() {
        return customerRepo.findAll().stream().map(this::toCustomerResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ApiResponse<CustomerResponse> findById(int id) {
        return customerRepo.findById(id)
                .map(customer -> ApiResponse.success("user fetched successfully", toCustomerResponse(customer)))
                .orElse(ApiResponse.notFound("Customer not found" + id));
    }

    @Override
    @Transactional
    public ApiResponse<CustomerResponse> create(CustomerRequest request) {
        if (customerRepo.findCustomerByEmail(request.getCustomerEmail()).isPresent()) {
            return ApiResponse.conflict("Customer already exists with this email" + request.getCustomerEmail());
        }
        Customer customer = new Customer();
        customer.setUserName(request.getCustomerName());
        customer.setEmail(request.getCustomerEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setPhoneNumber(PhoneUtil.normalize(request.getPhoneNumber()));

        Customer savedCustomer = customerRepo.save(customer);
        return ApiResponse.success("Customer created successfully", toCustomerResponse(savedCustomer));
    }


    @Override
    @Transactional
    public ApiResponse<CustomerResponse> update(int id, UpdateUserRequest request) {
        Customer customer = customerRepo.findById(id).orElse(null);
        if (customer == null)
            return ApiResponse.notFound("Customer not found" + id);

        ApiResponse<CustomerResponse> conflict = applyUpdate(customer, request);
        if (conflict != null) return conflict;

        customerRepo.save(customer);
        return ApiResponse.success("Customer updated successfully", toCustomerResponse(customer));
    }

    @Override
    @Transactional
    public ApiResponse<CustomerResponse> updateSelf(int id, UpdateUserRequest request) {
        Customer customer = customerRepo.findById(id).orElse(null);
        if (customer == null)
            return ApiResponse.notFound("Customer not found");

        if (!customer.isOtpVerified())
            return ApiResponse.forbidden("Verify your phone number via OTP before making changes to your account");

        ApiResponse<CustomerResponse> conflict = applyUpdate(customer, request);
        if (conflict != null) return conflict;

        customerRepo.save(customer);
        return ApiResponse.success("Customer updated successfully", toCustomerResponse(customer));
    }

    /**
     * Applies the requested field changes onto the given customer. Returns a
     * conflict response if the new email is already taken, or null if the
     * update was applied cleanly (caller is responsible for saving).
     */
    private ApiResponse<CustomerResponse> applyUpdate(Customer customer, UpdateUserRequest request) {
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            customer.setUserName(request.getUsername());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            boolean isEmailTaken = customerRepo.findCustomerByEmail(request.getEmail())
                    .filter(other -> other.getId() != customer.getId())
                    .isPresent();
            if (isEmailTaken)
                return ApiResponse.conflict("Customer already exists with this email" + request.getEmail());
            customer.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
            customer.setPhoneNumber(PhoneUtil.normalize(request.getPhoneNumber()));
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            customer.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return null;
    }

    @Override
    public ApiResponse<CustomerResponse> login(CustomerLoginRequest request) {
        return customerRepo.findCustomerByEmail(request.getEmail())
                .map(customer -> {
                    if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
                        return ApiResponse.<CustomerResponse>badRequest("Invalid email or password");
                    }

                    CustomerResponse response = toCustomerResponse(customer);
                    String message = customer.isOtpVerified()
                            ? "Login successful"
                            : "Login successful, but your phone number is not verified yet. Verify it via OTP to unlock full access.";
                    response.setMessage(message);
                    response.setToken(jwtService.generateToken(String.valueOf(customer.getId()), "CUSTOMER"));

                    return ApiResponse.success(message, response);
                })
                .orElse(ApiResponse.notFound("No account found with this email"));
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteById(int id) {
        return customerRepo.findById(id)
                .map(user -> {
                    user.setDeletedAt(java.time.LocalDateTime.now());
                    customerRepo.save(user);
                    return ApiResponse.<Void>success("User deleted successfully", null);
                })
                .orElse(ApiResponse.notFound("User not found: " + id));
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteSelf(int id) {
        return customerRepo.findById(id)
                .map(user -> {
                    if (!user.isOtpVerified())
                        return ApiResponse.<Void>forbidden("Verify your phone number via OTP before making changes to your account");

                    user.setDeletedAt(java.time.LocalDateTime.now());
                    customerRepo.save(user);
                    return ApiResponse.<Void>success("User deleted successfully", null);
                })
                .orElse(ApiResponse.notFound("Customer not found"));
    }

    @Override
    @Transactional
    public ApiResponse<String> sendOtp(String phoneNumber) {
        String normalizedPhone = PhoneUtil.normalize(phoneNumber);
        return customerRepo.findCustomerByPhoneNumber(normalizedPhone)
                .map(customer -> {
                    String otp = OtpUtil.generateCode();
                    customer.setOtpCode(otp);
                    customer.setOtpExpiresAt(LocalDateTime.now().plusMinutes(5));
                    customerRepo.save(customer);
                    // Dev-mode: no SMS gateway wired up yet, so the code is returned
                    // directly instead of being silently unreachable.
                    return ApiResponse.<String>success("OTP generated successfully (dev mode, no SMS sent)", otp);
                })
                .orElse(ApiResponse.notFound("No customer found with phoneNumber: " + normalizedPhone));
    }

    @Override
    @Transactional
    public ApiResponse<CustomerResponse> verifyOtp(String phoneNumber, String otp) {
        String normalizedPhone = PhoneUtil.normalize(phoneNumber);
        return customerRepo.findCustomerByPhoneNumber(normalizedPhone)
                .map(customer -> {
                    if (customer.getOtpCode() == null)
                        return ApiResponse.<CustomerResponse>badRequest("no otp sent to this phone number");
                    if (customer.getOtpExpiresAt().isBefore(LocalDateTime.now()))
                        return ApiResponse.<CustomerResponse>badRequest("otp code sent was expired");
                    if (!customer.getOtpCode().equals(otp))
                        return ApiResponse.<CustomerResponse>badRequest("invalid otp code");

                    customer.setOtpVerified(true);
                    customer.setOtpCode(null);
                    customer.setOtpExpiresAt(null);
                    customerRepo.save(customer);

                    return ApiResponse.success("customer phone number verified", toCustomerResponse(customer));
                })
                .orElse(ApiResponse.notFound("no customer found with this number " + normalizedPhone));
    }
}
