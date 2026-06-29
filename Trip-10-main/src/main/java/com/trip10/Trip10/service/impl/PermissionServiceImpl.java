package com.trip10.Trip10.service.impl;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.PermissionRequest;
import com.trip10.Trip10.entity.Permission;
import com.trip10.Trip10.repos.PermissionRepo;
import com.trip10.Trip10.service.PermissionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {


    private final PermissionRepo permissionRepo;

    @Autowired
    public PermissionServiceImpl(PermissionRepo permissionRepo) {
        this.permissionRepo = permissionRepo;
    }


    @Override
    public ApiResponse<List<Permission>> findAll() {
        return ApiResponse.success("permissions fetched successfully",permissionRepo.findAll());
    }

    @Override
    public ApiResponse<Permission> findById(int id) {
        return permissionRepo.findById(id)
                .map(permission -> ApiResponse.success("permission fetched successfully",permission))
                .orElse(ApiResponse.notFound("permission not found "+id));

    }

    @Override
    @Transactional
    public ApiResponse<Permission> create(PermissionRequest request) {
        if (permissionRepo.findById(request.getId()).isPresent()){
            return ApiResponse.conflict("permission id already exists "+request.getId());
        }
        if(permissionRepo.findByName(request.getName()).isPresent()){
            return ApiResponse.conflict("permission name already exists"+request.getName());
        }
        Permission saved =permissionRepo.save(new Permission(request.getId(), request.getName()));
        return ApiResponse.success("permission created successfully",saved);
    }

    @Override
    @Transactional
    public ApiResponse<Permission> update(int id, PermissionRequest request) {
        return permissionRepo.findById(id)
                .map(permission -> {
                    if (permissionRepo.findByName(request.getName()).isPresent()){
                        return ApiResponse.<Permission>conflict("permission with this name already exists"+request.getName());
                    }
                    permission.setName(request.getName());
                    permissionRepo.save(permission);
                    return ApiResponse.success("permission updated",permission);
                })
                .orElse(ApiResponse.notFound("permission not found "+id));
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteById(int id) {
        if (!permissionRepo.findById(id).isPresent()){
            return ApiResponse.notFound("permission not found "+id);
        }
        permissionRepo.deleteById(id);
        return ApiResponse.success("permission deleted successfully",null);
    }
}
