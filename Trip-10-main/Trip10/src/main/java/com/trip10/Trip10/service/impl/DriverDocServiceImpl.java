package com.trip10.Trip10.service.impl;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.CarDocResponse;
import com.trip10.Trip10.dto.DriverDocRequest;
import com.trip10.Trip10.dto.DriverDocResponse;
import com.trip10.Trip10.entity.AuthStatus;
import com.trip10.Trip10.entity.CarDoc;
import com.trip10.Trip10.entity.DriverDoc;
import com.trip10.Trip10.repos.DriverDocRepo;
import com.trip10.Trip10.service.DriverDocService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverDocServiceImpl implements DriverDocService {

    private final DriverDocRepo driverDocRepo;

    @Autowired
    public DriverDocServiceImpl(DriverDocRepo driverDocRepo) {
        this.driverDocRepo = driverDocRepo;
    }

    private DriverDocResponse toResponse(DriverDoc doc){

        DriverDocResponse response =new DriverDocResponse();
        response.setDriverId(doc.getDriverId());
        response.setPath(doc.getPath());
        response.setFileName(doc.getFileName());
        response.setUploadedAt(doc.getUploadedAt());
        response.setAuthStatus(doc.getAuthStatus());

        return response;
    }

    @Override
    public List<DriverDocResponse> findAll() {
       return driverDocRepo.findAll().stream().map(this::toResponse)
               .collect(Collectors.toList());
    }

    @Override
    public ApiResponse<DriverDocResponse> findById(int id) {
       return driverDocRepo.findById(id)
               .map(doc -> ApiResponse.success("document fetched successfully",toResponse(doc)))
               .orElse(ApiResponse.notFound("document not found "+id));
    }

    @Override
    @Transactional
    public ApiResponse<DriverDocResponse> create(DriverDocRequest request) {
        DriverDoc doc=new DriverDoc();
        doc.setDriverId(request.getDriverId());
        doc.setPath(request.getPath());
        doc.setFileName(request.getFileName());
        doc.setUploadedAt(request.getUploadedAt());
        doc.setAuthStatus(request.getAuthStatus());

        DriverDoc saved =driverDocRepo.save(doc);

        DriverDocResponse response =toResponse(saved);

        return ApiResponse.created("document created successfully", response);
    }

    @Override
    @Transactional
    public ApiResponse<DriverDocResponse> verifyDoc(int id, String s) {
        return (ApiResponse<DriverDocResponse>) driverDocRepo.findById(id)
                .map(doc -> {
                    if (doc.getAuthStatus()== AuthStatus.AUTHORIZED)
                        return ApiResponse.badRequest("document already verified");

                    doc.setAuthStatus(AuthStatus.AUTHORIZED);
                    driverDocRepo.save(doc);
                    DriverDocResponse response=toResponse(doc);
                    return ApiResponse.success("document verified successfully",response);
                }).orElse(ApiResponse.notFound("document not found"+id));

    }

    @Override
    public ApiResponse<DriverDocResponse> update(int id, DriverDocRequest request) {
        DriverDoc doc =driverDocRepo.findById(id).orElse(null);

        if (doc==null) return ApiResponse.notFound("document not found "+id);

        if (request.getFileName() !=null&&!request.getFileName().isBlank()){

            doc.setFileName(request.getFileName());
        }
        if (request.getPath()!=null && !request.getPath().isBlank()){
            doc.setPath(request.getPath());
        }
        driverDocRepo.save(doc);
        return ApiResponse.success("document updated successfully",toResponse(doc));

    }

    @Override
    public ApiResponse<Void> deleteById(int id) {
        return driverDocRepo.findById(id).map(
                doc -> {
                    driverDocRepo.delete(doc);
                    return ApiResponse.<Void>success("document deleted ",null);
                }
        ).orElse(ApiResponse.<Void>notFound("document not found" + id));

    }
}
