package com.trip10.Trip10.service;

import com.trip10.Trip10.entity.Admin;
import com.trip10.Trip10.entity.AdminPermission;
import com.trip10.Trip10.repos.AdminPermissionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionCheckService {

    private final AdminPermissionRepo adminPermissionRepo;

    @Autowired
    public PermissionCheckService(AdminPermissionRepo adminPermissionRepo) {
        this.adminPermissionRepo = adminPermissionRepo;
    }

    private AdminPermission getPermission(Admin admin) {
        return adminPermissionRepo.findById(admin.getPermissionId()).orElse(null);
    }

    public boolean canRejectDocuments(Admin admin) {

        if (admin.isSuperAdmin()) {
            return true;
        }

        AdminPermission permission = admin != null ? getPermission(admin) : null;
        return permission != null && permission.isCanRejectDocuments();
    }

    public boolean canVerifyDocuments(Admin admin) {

        if (admin.isSuperAdmin()) {
            return true;
        }

        AdminPermission permission = admin != null ? getPermission(admin) : null;
        return permission != null && permission.isCanVerifyDocuments();
    }

    public boolean canApproveDrivers(Admin admin) {
        if (admin.isSuperAdmin()) {
            return true;
        }

        AdminPermission permission = admin != null ? getPermission(admin) : null;
        return permission != null && permission.isCanApproveDrivers();
    }
    private boolean isSuperAdmin(Admin admin) {
        return admin != null && admin.isSuperAdmin();
    }
}
