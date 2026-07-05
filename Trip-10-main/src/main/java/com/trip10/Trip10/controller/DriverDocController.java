package com.trip10.Trip10.controller;

import com.trip10.Trip10.dto.*;
import com.trip10.Trip10.service.DriverDocService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ApiResponse<List<DriverDocResponse>> getDocs() {return ApiResponse.success("documents fetched successfully",driverDocService.findAll());}


@GetMapping("/{id}")
public ApiResponse<DriverDocResponse> getDoc(@PathVariable int id) {return driverDocService.findById(id);}


    @PostMapping("/add")
    public ApiResponse<DriverDocResponse> addDoc(@RequestBody DriverDocRequest request){
        return driverDocService.create(request);
    }

    @PutMapping("/{id}")
    public ApiResponse<DriverDocResponse> updateDoc(@PathVariable int id, @RequestBody DriverDocRequest request) {
        return driverDocService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDoc(@PathVariable int id){
        return driverDocService.deleteById(id);
    }


    @PostMapping("/verify/{id}")
    public ApiResponse<DriverDocResponse> verify(@PathVariable int id){

        return driverDocService.verifyDoc(id);

    }
    @PostMapping("/reject/{id}")
    public ApiResponse<DriverDocResponse>reject(@PathVariable int id,@RequestBody DriverDocRequest request){
        return driverDocService.rejectDoc(id,request);

    }

}

