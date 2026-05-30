package com.trip10.Trip10.dto;

import com.trip10.Trip10.entity.AuthStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DriverDocRequest {

    @NotBlank
    private int driverId;
    @NotBlank(message = "file name required")
    private String fileName;

    @NotBlank(message = "required")
    private String path;

    private LocalDateTime uploadedAt;

    private AuthStatus authStatus;

}
