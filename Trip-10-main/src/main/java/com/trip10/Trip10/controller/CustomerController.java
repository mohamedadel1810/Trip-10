package com.trip10.Trip10.controller;

import com.trip10.Trip10.service.CustomerService;
import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.CustomerRequest;
import com.trip10.Trip10.dto.CustomerResponse;
import com.trip10.Trip10.dto.UpdateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/customer")
public class CustomerController {
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomers() {

        return ApiResponse.success("customers fetched successfully",customerService.findAll()).toResponseEntity();

    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@PathVariable int id) {
        return customerService.findById(id).toResponseEntity();
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CustomerResponse>> addCustomer(@RequestBody CustomerRequest customerRequest) {
        return customerService.create(customerRequest).toResponseEntity();
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(@RequestBody UpdateUserRequest customerRequest, @PathVariable int id) {
        return customerService.update(id,customerRequest).toResponseEntity();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable int id) {
        return customerService.deleteById(id).toResponseEntity();
    }
}
