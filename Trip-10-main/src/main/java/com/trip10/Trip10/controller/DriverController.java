package com.trip10.Trip10.controller;

import com.trip10.Trip10.service.DriverService;
import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.DriverRequest;
import com.trip10.Trip10.dto.DriverResponse;
import com.trip10.Trip10.dto.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/driver")
public class DriverController {
    private final DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }
    @PostMapping
    public ResponseEntity<ApiResponse<DriverResponse>> addDriver(@RequestBody DriverRequest request){
        return driverService.create(request).toResponseEntity();
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<DriverResponse>> login(@RequestBody DriverRequest request){
        return driverService.login(request).toResponseEntity();
    }
    @GetMapping
    public ResponseEntity<ApiResponse<DriverResponse>> getSelf(Authentication authentication){
        return driverService.findById(Integer.parseInt(authentication.getName())).toResponseEntity();
    }
    @PutMapping
    public ResponseEntity<ApiResponse<DriverResponse>> updateSelf(Authentication authentication, @RequestBody UpdateUserRequest request){
        return driverService.updateSelf(Integer.parseInt(authentication.getName()), request).toResponseEntity();
    }
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteSelf(Authentication authentication){
        return driverService.deleteSelf(Integer.parseInt(authentication.getName())).toResponseEntity();
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
