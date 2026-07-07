package com.trip10.Trip10.service.impl;

import com.trip10.Trip10.dto.*;
import com.trip10.Trip10.entity.VerificationStatus;
import com.trip10.Trip10.entity.Document;
import com.trip10.Trip10.repos.DocumentRepo;
import com.trip10.Trip10.service.DocumentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepo documentRepo;

    public DocumentServiceImpl(DocumentRepo documentRepo) {
        this.documentRepo = documentRepo;
    }

    private DocumentResponse toResponse(Document document){

        DocumentResponse response=new DocumentResponse();
        response.setFileName(document.getFileName());
        response.setPath(document.getPath());
        response.setUploadAt(document.getUploadedAt());

        response.setDocumentType(document.getDocumentType());
        response.setOwnerType(document.getOwnerType());
        response.setUpdatedAt(document.getUpdatedAt());

        return response;

    }

    @Override
    public List<DocumentResponse> findAll() {
        return documentRepo.findAll().stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ApiResponse<DocumentResponse> findById(int id) {
       return documentRepo.findById(id).map(doc -> ApiResponse.success("Document fetched",toResponse(doc)))
               .orElse(ApiResponse.notFound("Document not found"+id));
    }

    @Override
    @Transactional
    public ApiResponse<DocumentResponse> create(DocumentRequest request) {
       Document doc=new Document();

        doc.setFileName(request.getFileName());
        doc.setPath(request.getPath());

        doc.setUploadedAt(request.getUploadAt());
        doc.setDocumentType(request.getDocumentType());
        doc.setOwnerType(request.getOwnerType());
        doc.setUpdatedAt(null);

        Document saved=documentRepo.save(doc);

        DocumentResponse response=toResponse(saved);
        return ApiResponse.created("Document added successfully",response);

    }

    @Override
    public ApiResponse<DocumentResponse> update(int id, DocumentRequest request) {

        Document doc=documentRepo.findById(id).orElse(null);

        if (doc==null) return ApiResponse.notFound("Document not found "+id);

        if (request.getFileName() !=null&&!request.getFileName().isBlank()){

            doc.setFileName(request.getFileName());}

        if (request.getPath()!=null && !request.getPath().isBlank()){
            doc.setPath(request.getPath());

            if (request.getOwnerType()!=null){
                doc.setOwnerType(request.getOwnerType());
            }

        }
        doc.setUpdatedAt(LocalDateTime.now());
        documentRepo.save(doc);
        return ApiResponse.success("Document updated successfully",toResponse(doc));

    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteById(int id) {
        return documentRepo.findById(id).map(
                document -> {
                    documentRepo.delete(document);

                    return ApiResponse.<Void>success("Document deleted ",null);
                }
        ).orElse(ApiResponse.<Void>notFound("Document not found" + id));
    }

//    @Override
//    @Transactional
//    public ApiResponse<DocumentResponse> verify(int id) {
//        return documentRepo.findById(id)
//                .map(doc -> {
//                    if (doc.getVerificationStatus()== VerificationStatus.VERIFIED)
//                        return ApiResponse.<DocumentResponse>badRequest("Document already verified");
//
//                    doc.setVerificationStatus(VerificationStatus.VERIFIED);
//                    documentRepo.save(doc);
//                    DocumentResponse response=toResponse(doc);
//                    return ApiResponse.success("Document verified successfully",response);
//                }).orElse(ApiResponse.notFound("Document not found"+id));
//    }
}

