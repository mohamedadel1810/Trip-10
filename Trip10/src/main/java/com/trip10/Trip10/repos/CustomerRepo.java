package com.trip10.Trip10.repos;

import com.trip10.Trip10.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {


    Optional<Customer> findCustomerByEmail(String email);

    Optional<Customer> findCustomerByPhoneNumber(String phoneNumber);


}
