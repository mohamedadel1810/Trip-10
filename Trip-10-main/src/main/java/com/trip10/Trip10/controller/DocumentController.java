package com.trip10.Trip10.controller;


import com.trip10.Trip10.dto.*;
import com.trip10.Trip10.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doc")
public class DocumentController {


    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DocumentResponse>>> getDocs() {
        return ApiResponse.success("documents fetched successfully", documentService.findAll()).toResponseEntity();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentResponse>> getDoc(@PathVariable int id) {
        return documentService.findById(id).toResponseEntity();
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse<DocumentResponse>> addDoc(@RequestBody DocumentRequest request) {
        return documentService.create(request).toResponseEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentResponse>> updateDoc(@PathVariable int id, @RequestBody DocumentRequest request) {
        return documentService.update(id, request).toResponseEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDoc(@PathVariable int id) {
        return documentService.deleteById(id).toResponseEntity();
    }

//
//    @PostMapping("/verify/{id}")
//    public ApiResponse<DocumentResponse> verify(@PathVariable int id) {
//        return documentService.verify(id);
//
//    }

}
