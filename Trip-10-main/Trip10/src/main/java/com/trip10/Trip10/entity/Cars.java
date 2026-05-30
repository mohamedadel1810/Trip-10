package com.trip10.Trip10.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cars")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cars {

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
}
