package com.trip10.Trip10.service.impl;

import com.trip10.Trip10.dto.AdminRequest;
import com.trip10.Trip10.dto.AdminResponse;
import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.entity.Admin;
import com.trip10.Trip10.repos.AdminRepo;
import com.trip10.Trip10.service.AdminService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

public class AdminServiceImpl implements AdminService {

    private final AdminRepo adminRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminServiceImpl(AdminRepo adminRepo, PasswordEncoder passwordEncoder) {
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
    }

    private AdminResponse toResponse(Admin admin){

        AdminResponse r =new AdminResponse();
        r.setId(admin.getId());
        r.setName(admin.getName());
        r.setEmail(admin.getEmail());
        return r;

    }

    @Override
    public List<AdminResponse> findAll() {
       List<Admin> admins= (List<Admin>) adminRepo.findAll();
       return admins.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public ApiResponse<AdminResponse> findById(int id) {
       return adminRepo.findById(id).map(admin -> ApiResponse.success("admin fetched successfully",toResponse(admin)))
               .orElse(ApiResponse.notFound("admin not found:"+id));
    }

    @Override
    public ApiResponse<AdminResponse> create(AdminRequest request) {
      Admin admin=new Admin();
      admin.setName(request.getName());
      admin.setEmail(request.getEmail());
      admin.setPassword(request.getPassword());

      Admin saved =adminRepo.save(admin);

      AdminResponse response=toResponse(saved);

      return ApiResponse.created("admin created successfully",response);
    }

    @Override
    public ApiResponse<AdminResponse> login(AdminRequest request) {
        return adminRepo.findAdminByEmail(request.getEmail()).map(admin -> {
            if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())){
                return ApiResponse.<AdminResponse>badRequest("invalid email or password");
            }
            AdminResponse response=new AdminResponse(
                    admin.getId(),
                    admin.getName(),
                    admin.getEmail()
            );
            return ApiResponse.success("login success",response);
        }).orElse(ApiResponse.notFound("no account found"));
    }

    @Override
    @Transactional
    public ApiResponse<AdminResponse> update(int id, AdminRequest request) {

        Admin admin=adminRepo.findById(id).orElse(null);
        if (admin==null)
            return ApiResponse.notFound("admin not found"+id);

        if (request.getName()!=null && !request.getName().isBlank()){
            admin.setName(request.getName());

            if (request.getEmail() !=null && !request.getEmail().isBlank()){
                boolean isEmailTaken =adminRepo.findAdminByEmail(request.getEmail())
                        .filter(admin1 -> admin.getId() !=id)
                        .isPresent();
                if (isEmailTaken)
                    return ApiResponse.conflict("admin already exists with this email"+request.getEmail());
                admin.setEmail(request.getEmail());
            }
            if (request.getPassword() !=null&& !request.getPassword().isBlank()){
                admin.setPassword(passwordEncoder.encode(request.getPassword()));
            }
        }
        adminRepo.save(admin);
        return ApiResponse.success("admin updated successfully",toResponse(admin));

    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteById(int id) {
        return adminRepo.findById(id)
                .map(admin -> {
                    adminRepo.delete(admin);
                    return ApiResponse.<Void>success("admin deleted",null);
                })
                .orElse(ApiResponse.notFound("admin not found :"+id));
    }
}
