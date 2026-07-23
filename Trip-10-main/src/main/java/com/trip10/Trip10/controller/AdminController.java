package com.trip10.Trip10.controller;

import com.trip10.Trip10.dto.AdminRequest;
import com.trip10.Trip10.dto.AdminResponse;
import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.LoginRequest;
import com.trip10.Trip10.service.AdminService;
import com.trip10.Trip10.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AdminController(AdminService adminService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.adminService = adminService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminResponse>>>getAll(){
        return ApiResponse.success("admin fetched ",adminService.findAll()).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminResponse>> getAdmin(@PathVariable int id){
        return adminService.findById(id).toResponseEntity();
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<AdminResponse>> addAdmin(@RequestBody AdminRequest request){
        return adminService.create(request).toResponseEntity();
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminResponse>> update(@PathVariable int id,@RequestBody AdminRequest request){

        return adminService.update(id,request).toResponseEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete (@PathVariable int id){

        return adminService.deleteById(id).toResponseEntity();
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest request) {


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token = jwtService.generateToken(request.getEmail(), "ADMIN");

        return ApiResponse.success(
                "Login successful",
                token
        ).toResponseEntity();
    }

}
