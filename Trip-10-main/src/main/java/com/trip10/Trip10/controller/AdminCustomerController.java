package com.trip10.Trip10.controller;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.CustomerResponse;
import com.trip10.Trip10.dto.UpdateUserRequest;
import com.trip10.Trip10.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/customer")
public class AdminCustomerController {

    private final CustomerService customerService;

    @Autowired
    public AdminCustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAll() {
        return ApiResponse.success("customers fetched successfully", customerService.findAll()).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomer(@PathVariable int id) {
        return customerService.findById(id).toResponseEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(@PathVariable int id, @RequestBody UpdateUserRequest request) {
        return customerService.update(id, request).toResponseEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable int id) {
        return customerService.deleteById(id).toResponseEntity();
    }
}
