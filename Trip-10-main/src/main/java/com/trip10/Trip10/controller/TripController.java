package com.trip10.Trip10.controller;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.TripRequest;
import com.trip10.Trip10.dto.TripResponse;
import com.trip10.Trip10.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/trip")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TripResponse>>> getAllTrips(){
        return ApiResponse.success("trips fetched successfully",tripService.findAll()).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TripResponse>> getTrip(@PathVariable int id){

        return tripService.findById(id).toResponseEntity();
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<TripResponse>> addTrip(@RequestBody TripRequest request){
        return tripService.create(request).toResponseEntity();
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TripResponse>>customerUpdate(@PathVariable int id ,@RequestBody TripRequest request){

        return tripService.customerUpdate(id, request).toResponseEntity();
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TripResponse>> adminUpdate(@PathVariable int id, @RequestBody TripRequest request){
        return tripService.adminUpdate(id,request).toResponseEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete (@PathVariable int id){
        return tripService.deleteById(id).toResponseEntity();
    }
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<TripResponse>> cancelTrip(@PathVariable int id,@RequestBody TripRequest request){

        return tripService.cancelTrip(id, request).toResponseEntity();
    }
}
