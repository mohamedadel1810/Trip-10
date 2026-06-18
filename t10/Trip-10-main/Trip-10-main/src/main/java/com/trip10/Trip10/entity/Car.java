package com.trip10.Trip10.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int carId;

    @Column(name = "brand",nullable = false)
    private String brand;

    @Column(name = "model",nullable = false)
    private String model;
    @Column(name = "color",nullable = false)
    private String color;
    @Column(name = "plate_number",nullable = false,unique = true)
    private String plateNumber;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
//
//
//    @ManyToOne
//    @JoinColumn(name = "driver_id")
//    private Driver driver;
//
//
//    @OneToMany(mappedBy = "car")
//    private List<CarDoc> docList=new ArrayList<>();



}
