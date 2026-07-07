package com.trip10.Trip10.controller;

import com.trip10.Trip10.dto.*;
import com.trip10.Trip10.service.DriverDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driverdoc")
public class DriverDocController {

    private final DriverDocService driverDocService;

    @Autowired
    public DriverDocController(DriverDocService driverDocService) {
        this.driverDocService = driverDocService;
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<DriverDocResponse>>> getDocs() {return ApiResponse.success("documents fetched successfully",driverDocService.findAll()).toResponseEntity();}


@GetMapping("/{id}")
public ResponseEntity<ApiResponse<DriverDocResponse>> getDoc(@PathVariable int id) {return driverDocService.findById(id).toResponseEntity();}


    @PostMapping("/add")
    public ResponseEntity<ApiResponse<DriverDocResponse>> addDoc(@RequestBody DriverDocRequest request){
        return driverDocService.create(request).toResponseEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DriverDocResponse>> updateDoc(@PathVariable int id, @RequestBody DriverDocRequest request) {
        return driverDocService.update(id, request).toResponseEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDoc(@PathVariable int id){
        return driverDocService.deleteById(id).toResponseEntity();
    }


    @PostMapping("/verify/{id}")
    public ResponseEntity<ApiResponse<DriverDocResponse>> verify(@PathVariable int id){

        return driverDocService.verifyDoc(id).toResponseEntity();

    }
    @PostMapping("/reject/{id}")
    public ResponseEntity<ApiResponse<DriverDocResponse>>reject(@PathVariable int id,@RequestBody DriverDocRequest request){
        return driverDocService.rejectDoc(id,request).toResponseEntity();

    }

}
