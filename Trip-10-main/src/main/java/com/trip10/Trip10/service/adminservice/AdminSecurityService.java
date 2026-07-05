package com.trip10.Trip10.service.adminservice;

import com.trip10.Trip10.entity.Admin;
import com.trip10.Trip10.repos.AdminRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminSecurityService {
    private final AdminRepo adminRepo;


    public Admin getCurrentAdmin(){

        String email= SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return adminRepo.findAdminByEmail(email).orElseThrow(()->new RuntimeException("admin not found"));
    }
}
