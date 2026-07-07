package com.trip10.Trip10.controller;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.CarRequest;
import com.trip10.Trip10.dto.CarResponse;
import com.trip10.Trip10.service.CarService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<CarResponse>>> getAllCars(){
        return ApiResponse.success("cars fetched ",carService.findAll()).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CarResponse>> getCarById(@PathVariable int id){

        return carService.findById(id).toResponseEntity();
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CarResponse>> addCar(@RequestBody CarRequest request){

        return carService.create(request).toResponseEntity();
    }

    @PostMapping("/verify/{id}")
    public ResponseEntity<ApiResponse<CarResponse>> verifyCar(@PathVariable int id,@RequestBody CarRequest request){

        return carService.verifyCar(id,request).toResponseEntity();

    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CarResponse>> updateCar (@RequestBody CarRequest request,int id){
        return carService.update(id,request).toResponseEntity();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCar(@PathVariable int id){
        return carService.deleteById(id).toResponseEntity();
    }
}
