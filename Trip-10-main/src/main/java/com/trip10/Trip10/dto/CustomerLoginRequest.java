package com.trip10.Trip10.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerLoginRequest {
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^(\\+20|0)(10|11|12|15)\\d{8}$",
            message = "Phone number must be a valid Egyptian number (e.g. 01012345678 or +201012345678)"
    )
    private String phoneNumber;

    @NotBlank(message = "password required")
    private String password;
}
