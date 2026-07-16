package com.trip10.Trip10.repos;

import com.trip10.Trip10.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepo extends JpaRepository<Trip,Integer> {
}
