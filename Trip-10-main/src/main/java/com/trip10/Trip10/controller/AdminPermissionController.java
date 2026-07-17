package com.trip10.Trip10.controller;

import com.trip10.Trip10.dto.AdminPermissionResponse;
import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.service.AdminPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin_permission")
public class AdminPermissionController {

    private final AdminPermissionService adminPermissionService;

    @Autowired
    public AdminPermissionController(AdminPermissionService adminPermissionService) {
        this.adminPermissionService = adminPermissionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminPermissionResponse>>> getAll() {
        return ApiResponse.success("admin permissions fetched", adminPermissionService.findAll()).toResponseEntity();
    }
}
