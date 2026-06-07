package com.trip10.Trip10.dto;

import com.trip10.Trip10.entity.AuthStatus;
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

    private AuthStatus authStatus=AuthStatus.PENDING;

    private DocumentType documentType;

}
