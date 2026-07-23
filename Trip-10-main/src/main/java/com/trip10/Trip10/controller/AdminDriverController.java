package com.trip10.Trip10.controller;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.DriverResponse;
import com.trip10.Trip10.dto.UpdateUserRequest;
import com.trip10.Trip10.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/driver")
public class AdminDriverController {

    private final DriverService driverService;

    @Autowired
    public AdminDriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DriverResponse>>> getAll() {
        return ApiResponse.success("drivers fetched successfully", driverService.findAll()).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DriverResponse>> getDriver(@PathVariable int id) {
        return driverService.findById(id).toResponseEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DriverResponse>> updateDriver(@PathVariable int id, @RequestBody UpdateUserRequest request) {
        return driverService.update(id, request).toResponseEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDriver(@PathVariable int id) {
        return driverService.deleteById(id).toResponseEntity();
    }
}
