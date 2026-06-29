package com.trip10.Trip10.controller;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.PermissionRequest;
import com.trip10.Trip10.entity.Permission;
import com.trip10.Trip10.service.PermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }


    @GetMapping
    public ApiResponse<List<Permission>> findAll(){
        return permissionService.findAll();
    }

    @GetMapping("/{id}")
    public ApiResponse<Permission> getById(@PathVariable int id) {
        return permissionService.findById(id);
    }

    @PostMapping
    public ApiResponse<Permission> create(@RequestBody PermissionRequest request) {
        return permissionService.create(request);
    }

    @PutMapping("/{id}")
    public ApiResponse<Permission> update(@PathVariable int id, @RequestBody PermissionRequest request) {
        return permissionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable int id) {
        return permissionService.deleteById(id);
    }

}
