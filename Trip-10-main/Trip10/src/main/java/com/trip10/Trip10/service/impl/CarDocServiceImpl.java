package com.trip10.Trip10.service.impl;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.CarDocRequest;
import com.trip10.Trip10.dto.CarDocResponse;
import com.trip10.Trip10.entity.AuthStatus;
import com.trip10.Trip10.entity.CarDoc;
import com.trip10.Trip10.repos.CarDocRepo;
import com.trip10.Trip10.service.CarDocService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarDocServiceImpl implements CarDocService {

    private final CarDocRepo carDocRepo;

    @Autowired
    public CarDocServiceImpl(CarDocRepo carDocRepo) {
        this.carDocRepo = carDocRepo;
    }

    private CarDocResponse toResponse(CarDoc carDoc){

        CarDocResponse doc=new CarDocResponse();
        doc.setCarId(carDoc.getCarId());
        doc.setPath(carDoc.getPath());
        doc.setFileName(carDoc.getFileName());
        doc.setUploadedAt(carDoc.getUploadedAt());
        doc.setAuthStatus(carDoc.getAuthStatus());

        return doc;
    }

    @Override
    public List<CarDocResponse> findAll() {
       return carDocRepo.findAll().stream().map(this::toResponse)
               .collect(Collectors.toList());
    }

    @Override
    public ApiResponse<CarDocResponse> findById(int id) {
       return carDocRepo.findById(id)
               .map(carDoc -> ApiResponse.success("document fetched",toResponse(carDoc)))
               .orElse(ApiResponse.notFound("document not found"+id));
    }

    @Override
    @Transactional
    public ApiResponse<CarDocResponse> create(CarDocRequest request) {
        CarDoc doc =new CarDoc();
        doc.setCarId(request.getCarId());
        doc.setPath(request.getPath());
        doc.setFileName(request.getFileName());
        doc.setUploadedAt(request.getUploadedAt());
        doc.setAuthStatus(request.getAuthStatus());

        CarDoc saved =carDocRepo.save(doc);

        CarDocResponse response =toResponse(saved);

        return ApiResponse.created("document added successfully",response);
    }

    @Override
    @Transactional
    public ApiResponse<CarDocResponse> verifyDoc(int id, String s) {
     return (ApiResponse<CarDocResponse>) carDocRepo.findById(id)
             .map(carDoc -> {
                 if (carDoc.getAuthStatus()== AuthStatus.AUTHORIZED)
                     return ApiResponse.badRequest("document already verified");

                 carDoc.setAuthStatus(AuthStatus.AUTHORIZED);
                 carDocRepo.save(carDoc);
                 CarDocResponse carDocResponse=toResponse(carDoc);
                 return ApiResponse.success("document verified successfully",carDocResponse);
             }).orElse(ApiResponse.notFound("document not found"+id));
    }

    @Override
    public ApiResponse<CarDocResponse> update(int id, CarDocRequest request) {
        CarDoc doc =carDocRepo.findById(id).orElse(null);

        if (doc==null) return ApiResponse.notFound("document not found "+id);

        if (request.getFileName() !=null&&!request.getFileName().isBlank()){

            doc.setFileName(request.getFileName());
        }
        if (request.getPath()!=null && !request.getPath().isBlank()){
            doc.setPath(request.getPath());
        }
        carDocRepo.save(doc);
        return ApiResponse.success("document updated successfully",toResponse(doc));
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteById(int id) {
     return carDocRepo.findById(id).map(
             carDoc -> {
                 carDocRepo.delete(carDoc);
                 return ApiResponse.<Void>success("document deleted ",null);
             }
     ).orElse(ApiResponse.<Void>notFound("document not found" + id));

    }
}
