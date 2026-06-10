package com.trip10.Trip10.service.impl;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.CarRequest;
import com.trip10.Trip10.dto.CarResponse;
import com.trip10.Trip10.entity.Car;
import com.trip10.Trip10.repos.CarRepo;
import com.trip10.Trip10.service.CarService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

private final CarRepo carRepo;

@Autowired
    public CarServiceImpl(CarRepo carRepo) {
        this.carRepo = carRepo;
    }
private CarResponse toCarResponse(Car car){

    return new CarResponse(car.getCarId(),car.getColor(),car.getBrand(), car.getModel(), car.getPlateNumber());
}

    @Override
    public List<CarResponse> findAll() {
        return carRepo.findAll().stream().map(this::toCarResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ApiResponse<CarResponse> findById(int id) {
        return carRepo.findById(id)
                .map(cars -> ApiResponse.success("car fetched successfully",toCarResponse(cars)))
                .orElse(ApiResponse.notFound("car not found "+ id));
    }

    @Override
    @Transactional
    public ApiResponse<CarResponse> create(CarRequest request) {
        Car cars =new Car();
        cars.setBrand(request.getBrand());
        cars.setColor(request.getColor());
        cars.setModel(request.getModel());
        cars.setPlateNumber(request.getPlateNumber());
        Car savedCar = carRepo.save(cars);

        return ApiResponse.success("car added ",toCarResponse(savedCar));
    }

//    @Override
//    public ApiResponse<CarResponse> verifyCar(int carId, String s) {
//        return null;
//    }

    @Override
    public ApiResponse<CarResponse> update(int id, CarRequest request) {
        Car car =carRepo.findById(id).orElse(null);

        if (car==null )
            return ApiResponse.notFound("car not found"+id);

        if (request.getBrand()!=null && !request.getBrand().isBlank()) {
            car.setBrand(request.getBrand());
        }

            if (request.getColor()!=null && !request.getColor().isBlank()) {
                car.setColor(request.getColor());
            }

                if (request.getModel()!=null && !request.getModel().isBlank()) {
                    car.setModel(request.getModel());
                }

                    if (request.getPlateNumber()!=null && !request.getPlateNumber().isBlank()){
                        car.setPlateNumber(request.getPlateNumber());
        }
                carRepo.save(car);
                return ApiResponse.success("car updated",toCarResponse(car));
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteById(int id) {
        return carRepo.findById(id)
                .map(car -> {
                    carRepo.delete(car);
                    return ApiResponse.<Void>success("Car deleted", null);
                })
                .orElse(ApiResponse.<Void>notFound("Car not found: " + id));
    }
}




