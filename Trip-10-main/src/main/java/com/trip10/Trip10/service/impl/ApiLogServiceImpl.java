package com.trip10.Trip10.service.impl;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.entity.ApiLog;
import com.trip10.Trip10.repos.ApiLogRepo;
import com.trip10.Trip10.service.ApiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiLogServiceImpl implements ApiLogService {

    private final ApiLogRepo apiLogRepo;

    @Autowired
    public ApiLogServiceImpl(ApiLogRepo apiLogRepo) {
        this.apiLogRepo = apiLogRepo;
    }

    @Override
    public ApiResponse<List<ApiLog>> findAll() {
        return ApiResponse.success("logs fetched successfully", apiLogRepo.findTop100ByOrderByIdDesc());
    }

    @Override
    public ApiResponse<ApiLog> findById(int id) {
        return apiLogRepo.findById(id)
                .map(log -> ApiResponse.success("log fetched successfully", log))
                .orElse(ApiResponse.notFound("log not found: " + id));
    }
}
