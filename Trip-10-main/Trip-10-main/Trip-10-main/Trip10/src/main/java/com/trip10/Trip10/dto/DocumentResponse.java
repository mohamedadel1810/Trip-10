package com.trip10.Trip10.dto;

import com.trip10.Trip10.entity.VerificationStatus;
import com.trip10.Trip10.entity.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DocumentResponse {

    private String fileName;

    private String path;

    private LocalDateTime uploadAt;

    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    private DocumentType documentType;

}
