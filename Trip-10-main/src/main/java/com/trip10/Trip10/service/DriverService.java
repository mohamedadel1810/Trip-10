package com.trip10.Trip10.service;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.DriverRequest;
import com.trip10.Trip10.dto.DriverResponse;
import com.trip10.Trip10.dto.UpdateUserRequest;

import java.util.List;

public interface DriverService {

List<DriverResponse> findAll();

ApiResponse<DriverResponse> findById(int id);

ApiResponse<DriverResponse> create(DriverRequest request);

ApiResponse<DriverResponse> verifyDriver(int driverID);
ApiResponse<String> sendOtp(String phoneNumber);


ApiResponse<DriverResponse>verifyOtp(String phoneNumber, String otp);

ApiResponse<DriverResponse> login(DriverRequest request);

ApiResponse<DriverResponse> update(int id, UpdateUserRequest request);
ApiResponse<DriverResponse> updateSelf(String email, UpdateUserRequest request);
ApiResponse<Void> deleteById(int id);
ApiResponse<Void> deleteSelf(String email);  }

//
//ApiResponse<DriverResponse> updateDoc(int id, DriverRequest request);
//}
