package com.trip10.Trip10.service;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.PermissionRequest;
import com.trip10.Trip10.entity.Permission;

import java.util.List;

public interface PermissionService {
    ApiResponse<List<Permission>> findAll();

    ApiResponse<Permission> findById(int id);

    ApiResponse<Permission> create(PermissionRequest request);

    ApiResponse<Permission>update (int id, PermissionRequest request);

    ApiResponse<Void> deleteById(int id);
}
