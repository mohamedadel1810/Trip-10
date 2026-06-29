package com.trip10.Trip10.service.impl;

import com.trip10.Trip10.dto.ApiResponse;
import com.trip10.Trip10.dto.CarDocRequest;
import com.trip10.Trip10.dto.CarDocResponse;
import com.trip10.Trip10.entity.Admin;
import com.trip10.Trip10.entity.VerificationStatus;
import com.trip10.Trip10.entity.CarDoc;
import com.trip10.Trip10.repos.AdminRepo;
import com.trip10.Trip10.repos.CarDocRepo;
import com.trip10.Trip10.service.CarDocService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarDocServiceImpl implements CarDocService {

    private final CarDocRepo carDocRepo;
    private final AdminRepo adminRepo;

    public CarDocServiceImpl(CarDocRepo carDocRepo, AdminRepo adminRepo) {
        this.carDocRepo = carDocRepo;
        this.adminRepo = adminRepo;
    }

    private CarDocResponse toResponse(CarDoc carDoc){

        CarDocResponse doc=new CarDocResponse();
        doc.setCarId(carDoc.getCarId());
        doc.setPath(carDoc.getPath());
        doc.setFileName(carDoc.getFileName());
        doc.setUploadedAt(carDoc.getUploadedAt());
        doc.setVerificationStatus(carDoc.getVerificationStatus());
        doc.setDocumentId(carDoc.getDocumentId());
        doc.setRejectionReason(carDoc.getRejectionReason());

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
               .map(carDoc -> ApiResponse.success("Document fetched",toResponse(carDoc)))
               .orElse(ApiResponse.notFound("Document not found"+id));
    }

    @Override
    @Transactional
    public ApiResponse<CarDocResponse> create(CarDocRequest request) {
        CarDoc doc =new CarDoc();
        doc.setCarId(request.getCarId());
        doc.setPath(request.getPath());
        doc.setFileName(request.getFileName());
        doc.setUploadedAt(request.getUploadedAt());
        doc.setVerificationStatus(request.getVerificationStatus());
        doc.setDocumentId(request.getDocumentId());
        doc.setRejectionReason(request.getRejectionReason());

        CarDoc saved =carDocRepo.save(doc);

        CarDocResponse response =toResponse(saved);

        return ApiResponse.created("Document added successfully",response);
    }

    @Override
    @Transactional
    public ApiResponse<CarDocResponse> verifyDoc(int id,CarDocRequest request,int adminId) {

       Optional<Admin> admin=adminRepo.findById(adminId);

       if (admin.isEmpty()){
           return ApiResponse.notFound("admin not found"+adminId);
       }

       Admin admin1=admin.get();

        if (!admin1.getPermission().isCanVerifyDocuments()){
            return ApiResponse.forbidden("this admin can not verify documents");
        }

     return carDocRepo.findById(id)
             .map(carDoc -> {
                 if (carDoc.getVerificationStatus()== VerificationStatus.VERIFIED)
                     return ApiResponse.<CarDocResponse>badRequest("Document already verified");

                 carDoc.setVerificationStatus(VerificationStatus.VERIFIED);
                 carDocRepo.save(carDoc);
                 CarDocResponse carDocResponse=toResponse(carDoc);
                 return ApiResponse.success("Document verified successfully",carDocResponse);
             }).orElse(ApiResponse.notFound("Document not found"+id));
    }

    @Override
    public ApiResponse<CarDocResponse> update(int id, CarDocRequest request) {
        CarDoc doc =carDocRepo.findById(id).orElse(null);

        if (doc==null) return ApiResponse.notFound("Document not found "+id);

        if (request.getFileName() !=null&&!request.getFileName().isBlank()){

            doc.setFileName(request.getFileName());
        }
        if (request.getPath()!=null && !request.getPath().isBlank()){
            doc.setPath(request.getPath());
        }
        carDocRepo.save(doc);
        return ApiResponse.success("Document updated successfully",toResponse(doc));
    }

    @Override
    @Transactional
    public ApiResponse<CarDocResponse> rejectDoc(int id, CarDocRequest request,int adminId) {

       Optional<Admin> admin=adminRepo.findById(adminId);
       if (admin.isEmpty()){
           return ApiResponse.notFound("admin not found"+adminId);
       }
Admin admin1=admin.get();
       if (!admin1.getPermission().isCanRejectDocuments()){
           return ApiResponse.forbidden("admin can not rejects documents");
       }

        return carDocRepo.findById(id).map(carDoc -> {

            if (carDoc.getVerificationStatus()==VerificationStatus.REJECTED)
                return ApiResponse.<CarDocResponse>badRequest("document already rejected");


            carDoc.setVerificationStatus(VerificationStatus.REJECTED);
            carDoc.setRejectionReason(request.getRejectionReason());
            carDocRepo.save(carDoc);
            CarDocResponse carDocResponse=toResponse(carDoc);
            return ApiResponse.success("document rejected ",carDocResponse);
        }).orElse(ApiResponse.notFound("document not found "+id));
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteById(int id) {
     return carDocRepo.findById(id).map(
             carDoc -> {
                 carDocRepo.delete(carDoc);
                 return ApiResponse.<Void>success("Document deleted ",null);
             }
     ).orElse(ApiResponse.<Void>notFound("Document not found" + id));

    }
}
