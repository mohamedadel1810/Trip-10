package com.trip10.Trip10.controller;

import com.trip10.Trip10.Service.CustomerService;
import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.CustomerRequest;
import com.trip10.Trip10.dto.CustomerResponse;
import com.trip10.Trip10.dto.UpdateUserRequest;
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
    public ApiResponse<List<CustomerResponse>> getAllCustomers() {

        return ApiResponse.success("customers fetched successfully",customerService.findAll());

    }
    @GetMapping("/{id}")
    public ApiResponse<CustomerResponse> getCustomerById(@PathVariable int id) {
        return customerService.findById(id);
    }
    @PostMapping("/add")
    public ApiResponse<CustomerResponse> addCustomer(@RequestBody CustomerRequest customerRequest) {
        return customerService.create(customerRequest);
    }
    @PutMapping("{/id}")
    public ApiResponse<CustomerResponse> updateCustomer(@RequestBody UpdateUserRequest customerRequest, @PathVariable int id) {
        return customerService.update(id,customerRequest);
    }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCustomer(@PathVariable int id) {
        return customerService.deleteById(id);
    }
}
