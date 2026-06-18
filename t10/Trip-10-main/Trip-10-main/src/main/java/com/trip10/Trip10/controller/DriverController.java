package com.trip10.Trip10.controller;

import com.trip10.Trip10.service.DriverService;
import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.DriverRequest;
import com.trip10.Trip10.dto.DriverResponse;
import com.trip10.Trip10.dto.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/driver")
public class DriverController {
    private final DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }
    @GetMapping
    public ApiResponse<List<DriverResponse>> getDrivers() {return ApiResponse.success("drivers fetched successfully",driverService.findAll());}

@GetMapping("/{id}")
    public ApiResponse<DriverResponse> getDriver(@PathVariable int id) {return driverService.findById(id);}

    @PostMapping("/add")
    public ApiResponse<DriverResponse> addDriver(@RequestBody DriverRequest request){
        return driverService.create(request);
    }
    @PutMapping("/{id}")
    public ApiResponse<DriverResponse> updateDriver(@PathVariable int id, @RequestBody UpdateUserRequest request){
        return driverService.update(id,request);
    }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDriver(@PathVariable int id){
        return driverService.deleteById(id);
    }

    @PutMapping("/{id]")
    public ApiResponse<DriverResponse> updateDriverDocs(@PathVariable int id, @RequestBody DriverRequest request){
        return driverService.updateDoc(id,request);
    }



}
