package com.trip10.Trip10.service;

import com.trip10.Trip10.dto.*;
import com.trip10.Trip10.entity.Document;

import java.util.List;

public interface DocumentService {

    List<DocumentResponse> findAll();
    ApiResponse<DocumentResponse> findById(int id);
    ApiResponse<DocumentResponse> create(DocumentRequest request);
    ApiResponse<DocumentResponse> update (int id, DocumentRequest request);
    ApiResponse<Void> deleteById(int id);

    ApiResponse<DocumentResponse>verify(int id);


}
