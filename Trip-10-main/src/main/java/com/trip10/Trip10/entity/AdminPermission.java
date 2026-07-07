package com.trip10.Trip10.entity;

import jakarta.persistence.*;
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


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private int permissionId;

    private boolean canVerifyDocuments;

    private boolean canRejectDocuments;

    private boolean canApproveDrivers;

    private boolean canManageAdmins;
}
