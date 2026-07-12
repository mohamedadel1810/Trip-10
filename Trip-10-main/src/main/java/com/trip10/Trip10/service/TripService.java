package com.trip10.Trip10.service;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.TripRequest;
import com.trip10.Trip10.dto.TripResponse;

import java.util.List;

public interface TripService {
    List<TripResponse> findAll();

    ApiResponse<TripResponse> findById(int id);

    ApiResponse<TripResponse> create(TripRequest request);

    ApiResponse<TripResponse> customerUpdate(int id,TripRequest request);


    ApiResponse<TripResponse> adminUpdate(int id,TripRequest request);

    ApiResponse<Void> deleteById(int id);

    ApiResponse<TripResponse> cancelTrip(int id,TripRequest request);

}
