package com.trip10.Trip10.dto;

import com.trip10.Trip10.entity.OwnerType;
import com.trip10.Trip10.entity.VerificationStatus;
import com.trip10.Trip10.entity.DocumentType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DocumentRequest {

    private int id;

    @NotBlank(message = "file name required")
    private String fileName;

    @NotBlank(message = "required")
    private String path;

    private LocalDateTime uploadAt;

    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    private DocumentType documentType;

    private OwnerType ownerType;

    private LocalDateTime updatedAt;

}
