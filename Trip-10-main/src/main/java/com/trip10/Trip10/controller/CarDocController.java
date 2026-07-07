package com.trip10.Trip10.controller;

import com.trip10.Trip10.dto.*;
import com.trip10.Trip10.service.CarDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/car/doc")
public class CarDocController {

    private final CarDocService carDocService;

    @Autowired
    public CarDocController(CarDocService carDocService) {
        this.carDocService = carDocService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CarDocResponse>>> getDocs() {return ApiResponse.success("documents fetched successfully",carDocService.findAll()).toResponseEntity();}

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CarDocResponse>> getDoc(@PathVariable int id) {return carDocService.findById(id).toResponseEntity();}

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CarDocResponse>> addDoc(@RequestBody CarDocRequest request){
        return carDocService.create(request).toResponseEntity();
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CarDocResponse>> updateDoc(@PathVariable int id, @RequestBody CarDocRequest request){
        return carDocService.update(id,request).toResponseEntity();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDoc(@PathVariable int id){
        return carDocService.deleteById(id).toResponseEntity();
    }


    @PostMapping("/verify/{id}")
    public ResponseEntity<ApiResponse<CarDocResponse>> verify(@PathVariable int id,@RequestBody CarDocRequest request){

        return carDocService.verifyDoc(id,request).toResponseEntity();

    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<ApiResponse<CarDocResponse>> reject(@PathVariable int id ,@RequestBody CarDocRequest request){
        return carDocService.rejectDoc(id,request).toResponseEntity();
    }
}
