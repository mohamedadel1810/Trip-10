package com.trip10.Trip10.service;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.CarDocRequest;
import com.trip10.Trip10.dto.CarDocResponse;

import java.util.List;

public interface CarDocService {

    List<CarDocResponse> findAll();


    ApiResponse<CarDocResponse> findById(int id);

    ApiResponse<CarDocResponse> create(CarDocRequest request);

    ApiResponse<CarDocResponse> verifyDoc(int id);

    ApiResponse<CarDocResponse>update(int id,CarDocRequest request);

    ApiResponse<Void> deleteById(int id);
}
