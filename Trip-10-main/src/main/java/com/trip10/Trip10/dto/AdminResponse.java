package com.trip10.Trip10.dto;

import com.trip10.Trip10.entity.AdminPermission;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminResponse {

    private int id;

    private String name;

    private String email;

    private  int permissionId;



}
