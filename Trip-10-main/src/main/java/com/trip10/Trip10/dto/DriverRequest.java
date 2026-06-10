package com.trip10.Trip10.dto;

import com.trip10.Trip10.entity.VerificationStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DriverRequest {

    @NotBlank(message = "driver name required")
    private String driverName;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^(\\+20|0)(10|11|12|15)\\d{8}$",
            message = "Phone number must be a valid Egyptian number (e.g. 01012345678 or +201012345678)"
    )
    private String phoneNumber;

    private VerificationStatus verificationStatus;


}
