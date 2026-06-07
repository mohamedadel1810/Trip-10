package com.trip10.Trip10.dto;

import com.trip10.Trip10.entity.AuthStatus;
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

    private AuthStatus authStatus=AuthStatus.PENDING;

    private DocumentType documentType;

}
