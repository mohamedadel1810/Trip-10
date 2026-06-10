package com.trip10.Trip10.service;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.CarRequest;
import com.trip10.Trip10.dto.CarResponse;

import java.util.List;

public interface CarService {

    List<CarResponse> findAll();

    ApiResponse<CarResponse> findById(int id);

    ApiResponse<CarResponse>  create (CarRequest request);

//    ApiResponse<CarResponse> verifyCar(int carId, String s);

    ApiResponse<CarResponse> update (int id, CarRequest request);

    ApiResponse<Void> deleteById(int id);
}
