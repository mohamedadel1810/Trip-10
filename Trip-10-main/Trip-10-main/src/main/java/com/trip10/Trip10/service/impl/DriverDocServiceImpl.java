package com.trip10.Trip10.service.impl;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.DriverDocRequest;
import com.trip10.Trip10.dto.DriverDocResponse;
import com.trip10.Trip10.entity.VerificationStatus;
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
        response.setVerificationStatus(doc.getVerificationStatus());
        response.setDocumentId(doc.getDocumentId());

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
               .map(doc -> ApiResponse.success("Document fetched successfully",toResponse(doc)))
               .orElse(ApiResponse.notFound("Document not found "+id));
    }

    @Override
    @Transactional
    public ApiResponse<DriverDocResponse> create(DriverDocRequest request) {
        DriverDoc doc=new DriverDoc();
        doc.setDriverId(request.getDriverId());
        doc.setPath(request.getPath());
        doc.setFileName(request.getFileName());
        doc.setDocumentId(request.getDocumentId());
        doc.setUploadedAt(request.getUploadedAt());
        doc.setVerificationStatus(request.getVerificationStatus());

        DriverDoc saved =driverDocRepo.save(doc);

        DriverDocResponse response =toResponse(saved);

        return ApiResponse.created("Document created successfully", response);
    }

    @Override
    @Transactional
    public ApiResponse<DriverDocResponse> verifyDoc(int id) {
        return (ApiResponse<DriverDocResponse>) driverDocRepo.findById(id)
                .map(doc -> {
                    if (doc.getVerificationStatus()== VerificationStatus.VERIFIED)
                        return ApiResponse.badRequest("Document already verified");

                    doc.setVerificationStatus(VerificationStatus.VERIFIED);
                    driverDocRepo.save(doc);
                    DriverDocResponse response=toResponse(doc);
                    return ApiResponse.success("Document verified successfully",response);
                }).orElse(ApiResponse.notFound("Document not found"+id));

    }

    @Override
    public ApiResponse<DriverDocResponse> update(int id, DriverDocRequest request) {
        DriverDoc doc =driverDocRepo.findById(id).orElse(null);

        if (doc==null) return ApiResponse.notFound("Document not found "+id);

        if (request.getFileName() !=null&&!request.getFileName().isBlank()){

            doc.setFileName(request.getFileName());
        }
        if (request.getPath()!=null && !request.getPath().isBlank()){
            doc.setPath(request.getPath());
        }
        driverDocRepo.save(doc);
        return ApiResponse.success("Document updated successfully",toResponse(doc));

    }

    @Override
    public ApiResponse<Void> deleteById(int id) {
        return driverDocRepo.findById(id).map(
                doc -> {
                    driverDocRepo.delete(doc);
                    return ApiResponse.<Void>success("Document deleted ",null);
                }
        ).orElse(ApiResponse.<Void>notFound("Document not found" + id));

    }
}
