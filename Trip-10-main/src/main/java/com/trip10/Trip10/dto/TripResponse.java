package com.trip10.Trip10.dto;

import com.trip10.Trip10.entity.TripStatus;
import com.trip10.Trip10.entity.TripType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TripResponse {

    int id;

    int driverId;

    int customerId;

    String startLocation;

    String endLocation;

    double distance;

    double price;

    LocalDateTime startTime;

    LocalDateTime endTime;

    LocalDateTime updatedAt;

    LocalDateTime deletedAt;

    TripStatus tripStatus;

    TripType tripType;

}
