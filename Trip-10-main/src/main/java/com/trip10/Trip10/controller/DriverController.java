package com.trip10.Trip10.controller;

import com.trip10.Trip10.service.DriverService;
import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.DriverRequest;
import com.trip10.Trip10.dto.DriverResponse;
import com.trip10.Trip10.dto.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<DriverResponse>>> getDrivers() {return ApiResponse.success("drivers fetched successfully",driverService.findAll()).toResponseEntity();}

@GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DriverResponse>> getDriver(@PathVariable int id) {return driverService.findById(id).toResponseEntity();}

    @PostMapping
    public ResponseEntity<ApiResponse<DriverResponse>> addDriver(@RequestBody DriverRequest request){
        return driverService.create(request).toResponseEntity();
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<DriverResponse>> login(@RequestBody DriverRequest request){
        return driverService.login(request).toResponseEntity();
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DriverResponse>> updateDriver(@PathVariable int id, @RequestBody UpdateUserRequest request){
        return driverService.update(id,request).toResponseEntity();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDriver(@PathVariable int id){
        return driverService.deleteById(id).toResponseEntity();
    }

@PostMapping("/otp/send")
    public ResponseEntity<ApiResponse<String>> sendOTP(@RequestParam String phoneNumber){

        return driverService.sendOtp(phoneNumber).toResponseEntity();

}
@PostMapping("/verify/{id}")
public ResponseEntity<ApiResponse<DriverResponse>> verifyDriver(@PathVariable int id){
        return driverService.verifyDriver(id).toResponseEntity();
}

@PostMapping("/phone/verify")
public ResponseEntity<ApiResponse<DriverResponse>>verifyNumber(@RequestParam String phoneNumber,@RequestParam String otpCode){
        return driverService.verifyOtp(phoneNumber,otpCode).toResponseEntity();
}




//    @PutMapping("/{id]")
//    public ApiResponse<DriverResponse> updateDriverDocs(@PathVariable int id, @RequestBody DriverRequest request){
//        return driverService.updateDoc(id,request);
//    }



}
