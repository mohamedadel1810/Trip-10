package com.trip10.Trip10.controller;

import com.trip10.Trip10.service.CustomerService;
import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.CustomerLoginRequest;
import com.trip10.Trip10.dto.CustomerRequest;
import com.trip10.Trip10.dto.CustomerResponse;
import com.trip10.Trip10.dto.UpdateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/customer")
public class CustomerController {
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<CustomerResponse>> login(@RequestBody CustomerLoginRequest request) {
        return customerService.login(request).toResponseEntity();
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CustomerResponse>> addCustomer(@RequestBody CustomerRequest customerRequest) {
        return customerService.create(customerRequest).toResponseEntity();
    }
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateSelf(Authentication authentication, @RequestBody UpdateUserRequest request) {
        return customerService.updateSelf(authentication.getName(), request).toResponseEntity();
    }
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteSelf(Authentication authentication) {
        return customerService.deleteSelf(authentication.getName()).toResponseEntity();
    }

    @PostMapping("/otp/send")
    public ResponseEntity<ApiResponse<String>> sendOTP(@RequestParam String phoneNumber) {
        return customerService.sendOtp(phoneNumber).toResponseEntity();
    }

    @PostMapping("/phone/verify")
    public ResponseEntity<ApiResponse<CustomerResponse>> verifyNumber(@RequestParam String phoneNumber, @RequestParam String otpCode) {
        return customerService.verifyOtp(phoneNumber, otpCode).toResponseEntity();
    }
}
