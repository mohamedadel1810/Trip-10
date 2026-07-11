package com.trip10.Trip10.controller;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.entity.ApiLog;
import com.trip10.Trip10.service.ApiLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class ApiLogController {

    private final ApiLogService apiLogService;

    public ApiLogController(ApiLogService apiLogService) {
        this.apiLogService = apiLogService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApiLog>>> getLogs() {
        return apiLogService.findAll().toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApiLog>> getLog(@PathVariable int id) {
        return apiLogService.findById(id).toResponseEntity();
    }
}
