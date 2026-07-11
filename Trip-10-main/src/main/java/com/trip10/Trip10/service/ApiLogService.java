package com.trip10.Trip10.service;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.entity.ApiLog;

import java.util.List;

public interface ApiLogService {
    ApiResponse<List<ApiLog>> findAll();

    ApiResponse<ApiLog> findById(int id);
}
