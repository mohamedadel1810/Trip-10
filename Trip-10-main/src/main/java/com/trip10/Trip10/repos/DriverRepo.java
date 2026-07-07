package com.trip10.Trip10.repos;

import com.trip10.Trip10.entity.Driver;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DriverRepo extends CrudRepository<Driver, Integer> {

    Optional<Driver> findDriverByEmail(String email);

    Optional<Driver> findDriverByPhoneNumber(String phoneNumber);
}
