package com.trip10.Trip10.controller;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.PermissionRequest;
import com.trip10.Trip10.entity.Permission;
import com.trip10.Trip10.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<Permission>>> findAll(){
        return permissionService.findAll().toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Permission>> getById(@PathVariable int id) {
        return permissionService.findById(id).toResponseEntity();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Permission>> create(@RequestBody PermissionRequest request) {
        return permissionService.create(request).toResponseEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Permission>> update(@PathVariable int id, @RequestBody PermissionRequest request) {
        return permissionService.update(id, request).toResponseEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable int id) {
        return permissionService.deleteById(id).toResponseEntity();
    }

}
