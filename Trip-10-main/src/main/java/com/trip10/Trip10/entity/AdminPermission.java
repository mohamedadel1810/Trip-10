package com.trip10.Trip10.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admin_permission")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminPermission {


    private int permission_id;

    private boolean canVerifyDocuments;

    private boolean canRejectDocuments;

    private boolean canApproveDrivers;

    private boolean canManageAdmins;
}
