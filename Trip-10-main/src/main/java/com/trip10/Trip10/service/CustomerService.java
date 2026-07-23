package com.trip10.Trip10.service;

import com.trip10.Trip10.dto.*;

import java.util.List;

public interface CustomerService {

    List<CustomerResponse> findAll();
    ApiResponse<CustomerResponse> findById(int id);
    ApiResponse<CustomerResponse> create(CustomerRequest request);
    ApiResponse<CustomerResponse> update (int id, UpdateUserRequest request);
    ApiResponse<CustomerResponse> updateSelf(int id, UpdateUserRequest request);
    ApiResponse<CustomerResponse> login (CustomerLoginRequest request);
    ApiResponse<Void> deleteById(int id);
    ApiResponse<Void> deleteSelf(int id);
    ApiResponse<String> sendOtp(String phoneNumber);
    ApiResponse<CustomerResponse> verifyOtp(String phoneNumber, String otp);
}

