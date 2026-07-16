package com.trip10.Trip10.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "trip")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Trip {

    @Id
    @Column(name = "trip_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int tripId;

    @Column(name = "driver_id")
    int driverId;

    @Column(name = "customer_id")
    int customerId;

    @Column(name = "start_location")
    String startLocation;

    @Column(name= "end_location")
    String endLocation;

    @Column(name = "distance")
    double distance;

    @Column(name = "price")
    double price;

    @Column(name = "start_time")
    LocalDateTime startTime;
    @Column(name = "end_time")
    LocalDateTime endTime;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "trip_status")
    TripStatus tripStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "trip_type")
    TripType tripType;




}
