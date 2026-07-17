package com.trip10.Trip10.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminPermissionResponse {

    private int id;

    private boolean canVerifyDocuments;

    private boolean canRejectDocuments;

    private boolean canApproveDrivers;

    private boolean canManageAdmins;

}
