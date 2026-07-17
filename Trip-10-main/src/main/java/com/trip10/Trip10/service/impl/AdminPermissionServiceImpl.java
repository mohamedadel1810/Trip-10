package com.trip10.Trip10.service.impl;

import com.trip10.Trip10.dto.AdminPermissionResponse;
import com.trip10.Trip10.entity.AdminPermission;
import com.trip10.Trip10.repos.AdminPermissionRepo;
import com.trip10.Trip10.service.AdminPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminPermissionServiceImpl implements AdminPermissionService {

    private final AdminPermissionRepo adminPermissionRepo;

    @Autowired
    public AdminPermissionServiceImpl(AdminPermissionRepo adminPermissionRepo) {
        this.adminPermissionRepo = adminPermissionRepo;
    }

    private AdminPermissionResponse toResponse(AdminPermission permission) {
        AdminPermissionResponse r = new AdminPermissionResponse();
        r.setId(permission.getId());
        r.setCanVerifyDocuments(permission.isCanVerifyDocuments());
        r.setCanRejectDocuments(permission.isCanRejectDocuments());
        r.setCanApproveDrivers(permission.isCanApproveDrivers());
        r.setCanManageAdmins(permission.isCanManageAdmins());
        return r;
    }

    @Override
    public List<AdminPermissionResponse> findAll() {
        return adminPermissionRepo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }
}
