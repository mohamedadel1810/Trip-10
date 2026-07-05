package com.trip10.Trip10.service;

import com.trip10.Trip10.entity.Admin;
import org.springframework.stereotype.Service;

@Service
public class PermissionCheckService {

    public boolean canRejectDocuments(Admin admin) {

        if (admin.isSuperAdmin()) {
            return true;
        }

        return admin != null
                && admin.getPermission() != null
                && admin.getPermission().isCanRejectDocuments();
    }

    public boolean canVerifyDocuments(Admin admin) {

        if (admin.isSuperAdmin()) {
            return true;
        }

        return admin != null
                && admin.getPermission() != null
                && admin.getPermission().isCanVerifyDocuments();
    }

    public boolean canApproveDrivers(Admin admin) {
        if (admin.isSuperAdmin()) {
            return true;
        }

        return admin != null
                && admin.getPermission() != null
                && admin.getPermission().isCanApproveDrivers();
    }
    private boolean isSuperAdmin(Admin admin) {
        return admin != null && admin.isSuperAdmin();
    }
}
