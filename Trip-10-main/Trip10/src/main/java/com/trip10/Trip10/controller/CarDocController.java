package com.trip10.Trip10.controller;

import com.trip10.Trip10.dto.*;
import com.trip10.Trip10.service.CarDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cardoc")
public class CarDocController {

    private final CarDocService carDocService;

    @Autowired
    public CarDocController(CarDocService carDocService) {
        this.carDocService = carDocService;
    }

    @GetMapping
    public ApiResponse<List<CarDocResponse>> getDocs() {return ApiResponse.success("documents fetched successfully",carDocService.findAll());}

    @GetMapping("/{id}")
    public ApiResponse<CarDocResponse> getDoc(@PathVariable int id) {return carDocService.findById(id);}

    @PostMapping("/add")
    public ApiResponse<CarDocResponse> addDoc(@RequestBody CarDocRequest request){
        return carDocService.create(request);
    }
    @PutMapping("/{id}")
    public ApiResponse<CarDocResponse> updateDoc(@PathVariable int id, @RequestBody CarDocRequest request){
        return carDocService.update(id,request);
    }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDoc(@PathVariable int id){
        return carDocService.deleteById(id);
    }



}
