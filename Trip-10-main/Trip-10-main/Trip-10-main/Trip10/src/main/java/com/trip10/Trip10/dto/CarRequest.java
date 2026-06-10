package com.trip10.Trip10.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarRequest {

    @NotBlank(message = "should insert color")
    private String color;

    @NotBlank(message ="should insert brand" )
    private String brand;

    @NotBlank(message = "should insert model")
    private String model;

    @NotBlank(message = "should insert plate number ")
    private String plateNumber;

}
