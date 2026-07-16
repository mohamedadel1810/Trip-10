package com.trip10.Trip10.service.impl;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.TripRequest;
import com.trip10.Trip10.dto.TripResponse;
import com.trip10.Trip10.entity.Trip;
import com.trip10.Trip10.entity.TripStatus;
import com.trip10.Trip10.repos.TripRepo;
import com.trip10.Trip10.service.TripService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripServiceImpl implements TripService {

    private final TripRepo tripRepo;

    @Autowired
    public TripServiceImpl(TripRepo tripRepo) {
        this.tripRepo = tripRepo;
    }

    private TripResponse toResponse(Trip trip){
        TripResponse t=new TripResponse();
        t.setId(trip.getId());
        t.setCustomerId(trip.getCustomerId());
        t.setDriverId(trip.getDriverId());
        t.setDistance(trip.getDistance());
        t.setPrice(trip.getPrice());
        t.setStartLocation(trip.getStartLocation());
        t.setEndLocation(trip.getEndLocation());
        t.setStartTime(trip.getStartTime());
        t.setEndTime(trip.getEndTime());
        t.setUpdatedAt(trip.getUpdatedAt());
        t.setDeletedAt(trip.getDeletedAt());
        t.setTripStatus(trip.getTripStatus());
        t.setTripType(trip.getTripType());
        return t;
    }

    @Override
    public List<TripResponse> findAll() {
        return tripRepo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public ApiResponse<TripResponse> findById(int id) {
        return tripRepo.findById(id).map(trip -> ApiResponse.success("trip fetched successfully",toResponse(trip)))
                .orElse(ApiResponse.notFound("trip not found:"+id));
    }

    @Override
    public ApiResponse<TripResponse> create(TripRequest request) {
        Trip trip=new Trip();
        trip.setCustomerId(request.getCustomerId());
        trip.setDriverId(request.getDriverId());
        trip.setPrice(request.getPrice());
        trip.setStartLocation(request.getStartLocation());
        trip.setEndLocation(request.getEndLocation());
        trip.setDistance(request.getDistance());
        trip.setStartTime(request.getStartTime());
        trip.setEndTime(request.getEndTime());
        trip.setUpdatedAt(request.getUpdatedAt());
        trip.setDeletedAt(request.getDeletedAt());
        trip.setTripStatus(request.getTripStatus());
        trip.setTripType(request.getTripType());

        Trip saved=tripRepo.save(trip);

        TripResponse response=toResponse(saved);
        return ApiResponse.created("trip created successfully",response);
    }

    @Override
    @Transactional
    public ApiResponse<TripResponse> customerUpdate(int id, TripRequest request) {
        Trip trip=tripRepo.findById(id).orElse(null);
        if (trip==null)
            return ApiResponse.notFound("trip not found"+id);

        if (request.getStartLocation()!=null && !request.getStartLocation().isBlank()){
            trip.setStartLocation(request.getStartLocation());

            if (request.getEndLocation()!=null && !request.getEndLocation().isBlank()){
                trip.setEndTime(request.getEndTime());

                if (request.getTripStatus()!=null && !request.getTripStatus().equals(null)){
                    trip.setTripStatus(request.getTripStatus());
                }

            }
        }
        tripRepo.save(trip);
        return ApiResponse.success("trip updated",toResponse(trip));
    }

    @Override
    @Transactional
    public ApiResponse<TripResponse> adminUpdate(int id, TripRequest request) {
        Trip trip =tripRepo.findById(id).orElse(null);
        if (trip==null)
            return ApiResponse.notFound("trip not found"+id);

        if (request.getPrice() >= 0){
            trip.setPrice(request.getPrice());

            if (request.getTripStatus()!=null && !request.getTripStatus().equals(null)){
                trip.setTripStatus(request.getTripStatus());
            }

        }
        tripRepo.save(trip);
        return ApiResponse.success("price updated successfully",toResponse(trip));
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteById(int id) {
        return tripRepo.findById(id).map(trip -> {tripRepo.delete(trip);
        return ApiResponse.<Void>success("trip deleted/canceled",null);
        }).orElse(ApiResponse.notFound("trip not found:"+id));
    }

    @Override
    public ApiResponse<TripResponse> cancelTrip(int id, TripRequest request) {
        Trip trip =tripRepo.findById(id).orElse(null);
        if (trip==null) {
            return ApiResponse.notFound("trip not found. " + id);
        }
        trip.setTripStatus(TripStatus.CANCELED);
        return ApiResponse.success("trip canceled..",toResponse(trip));
    }
}
