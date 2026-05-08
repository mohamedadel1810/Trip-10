package com.trip10.Trip10.Service.Impl;

import com.trip10.Trip10.Service.CustomerService;
import com.trip10.Trip10.dto.*;
import com.trip10.Trip10.entity.Customer;
import com.trip10.Trip10.repos.CustomerRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepo customerRepo;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerServiceImpl(CustomerRepo customerRepo, PasswordEncoder passwordEncoder) {
        this.customerRepo = customerRepo;
        this.passwordEncoder = passwordEncoder;
    }

    private CustomerResponse toCustomerResponse(Customer customer) {
        return new CustomerResponse(customer.getId(), customer.getUserName(), customer.getEmail(), customer.getPhoneNumber());
    }

    @Override
    public List<CustomerResponse> findAll() {
        return customerRepo.findAll().stream().map(this::toCustomerResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ApiResponse<CustomerResponse> findById(int id) {
        return customerRepo.findById(id)
                .map(customer -> ApiResponse.success("user fetched successfully", toCustomerResponse(customer)))
                .orElse(ApiResponse.notFound("Customer not found" + id));
    }

    @Override
    @Transactional
    public ApiResponse<CustomerResponse> create(CustomerRequest request) {
        if (customerRepo.findCustomerByEmail(request.getCustomerEmail()).isPresent()) {
            return ApiResponse.conflict("Customer already exists with this email" + request.getCustomerEmail());
        }
        Customer customer = new Customer();
        customer.setUserName(request.getCustomerName());
        customer.setEmail(request.getCustomerEmail());
        customer.setPassword(request.getPassword());
        customer.setPhoneNumber(request.getPhoneNumber());

        Customer savedCustomer = customerRepo.save(customer);
        return ApiResponse.success("Customer created successfully", toCustomerResponse(savedCustomer));
    }


    @Override
    @Transactional
    public ApiResponse<CustomerResponse> update(int id, UpdateUserRequest request) {
        Customer customer = customerRepo.findById(id).orElse(null);
        if (customer == null)
            return ApiResponse.notFound("Customer not found" + id);

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            customer.setUserName(request.getUsername());

            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                boolean isEmailTaken = customerRepo.findCustomerByEmail(request.getEmail())
                        .filter(other -> other.getId() != id)
                        .isPresent();
                if (isEmailTaken)
                    return ApiResponse.conflict("Customer already exists with this email" + request.getEmail());
                customer.setEmail(request.getEmail());
            }
            if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
                customer.setPhoneNumber(request.getPhoneNumber());
            }
            if (request.getPassword() != null && !request.getPassword().isBlank()) {
                customer.setPassword(passwordEncoder.encode(request.getPassword()));
            }

        }
        customerRepo.save(customer);
        return ApiResponse.success("Customer updated successfully", toCustomerResponse(customer));
    }

    @Override
    public ApiResponse<CustomerResponse> login(CustomerLoginRequest request) {
        return customerRepo.findCustomerByEmail(request.getEmail())
                .map(customer -> {
                    if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
                        return ApiResponse.<CustomerResponse>badRequest("Invalid email or password");
                    }

                    CustomerResponse response = new CustomerResponse(
                            customer.getId(),
                            customer.getUserName(),
                            customer.getEmail(),
                            customer.getPhoneNumber()
                    );

                    return ApiResponse.success("Login successful", response);
                })
                .orElse(ApiResponse.notFound("No account found with this email"));
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteById(int id) {
        return customerRepo.findById(id)
                .map(user -> {
                    user.setDeletedAt(java.time.LocalDateTime.now());
                    customerRepo.save(user);
                    return ApiResponse.<Void>success("User deleted successfully", null);
                })
                .orElse(ApiResponse.notFound("User not found: " + id));
    }
}
