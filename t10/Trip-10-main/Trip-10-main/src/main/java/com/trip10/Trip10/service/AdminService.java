package com.trip10.Trip10.service;

import com.trip10.Trip10.dto.AdminRequest;
import com.trip10.Trip10.dto.AdminResponse;
import com.trip10.Trip10.dto.ApiResponse;

import java.util.List;

public interface AdminService {


    List<AdminResponse> findAll();

    ApiResponse<AdminResponse> findById(int id);

    ApiResponse<AdminResponse> create(AdminRequest request);

    ApiResponse<AdminResponse> login(AdminRequest request);

    ApiResponse<AdminResponse> update (int id,AdminRequest request);

    ApiResponse<Void> deleteById(int id);

}
