package com.trip10.Trip10.controller;

import com.trip10.Trip10.dto.AdminRequest;
import com.trip10.Trip10.dto.AdminResponse;
import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @GetMapping
    public ApiResponse<List<AdminResponse>>getAll(){
        return ApiResponse.success("admin fetched ",adminService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<AdminResponse> getAdmin(@PathVariable int id){
        return adminService.findById(id);
    }
    @PostMapping("/add")
    public ApiResponse<AdminResponse> addAdmin(@RequestBody AdminRequest request){
        return adminService.create(request);
    }
    @PutMapping("/{id}")
    public ApiResponse<AdminResponse> update(@PathVariable int id,@RequestBody AdminRequest request){

        return adminService.update(id,request);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete (@PathVariable int id){

        return adminService.deleteById(id);
    }
}
