package com.trip10.Trip10.service;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.DriverDocRequest;
import com.trip10.Trip10.dto.DriverDocResponse;

import java.util.List;

public interface DriverDocService {

    List<DriverDocResponse> findAll();

    ApiResponse<DriverDocResponse> findById(int id);

    ApiResponse<DriverDocResponse> create(DriverDocRequest request);

    ApiResponse<DriverDocResponse> verifyDoc(int id,int adminId);

    ApiResponse<DriverDocResponse> rejectDoc(int id,DriverDocRequest request,int adminId);

    ApiResponse<DriverDocResponse> update(int id , DriverDocRequest request);

    ApiResponse<Void> deleteById(int id);
}
