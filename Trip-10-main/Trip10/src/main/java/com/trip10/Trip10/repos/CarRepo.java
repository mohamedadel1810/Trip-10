package com.trip10.Trip10.repos;

import com.trip10.Trip10.entity.Cars;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepo extends JpaRepository<Cars,Integer> {
}
