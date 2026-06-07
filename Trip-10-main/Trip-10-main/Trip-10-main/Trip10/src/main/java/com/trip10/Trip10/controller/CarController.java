package com.trip10.Trip10.controller;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.CarRequest;
import com.trip10.Trip10.dto.CarResponse;
import com.trip10.Trip10.service.CarService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/car")
public class CarController {

    private CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }
    @GetMapping
    public ApiResponse<List<CarResponse>> getAllCars(){
        return ApiResponse.success("cars fetched ",carService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<CarResponse> getCarById(@PathVariable int id){

        return carService.findById(id);
    }

    @PostMapping("/add")
    public ApiResponse<CarResponse> addCar(@RequestBody CarRequest request){

        return carService.create(request);
    }

    @PutMapping("/{id}")
    public ApiResponse<CarResponse> updateCar (@RequestBody CarRequest request,int id){
        return carService.update(id,request);
    }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCar(@PathVariable int id){
        return carService.deleteById(id);
    }
}
