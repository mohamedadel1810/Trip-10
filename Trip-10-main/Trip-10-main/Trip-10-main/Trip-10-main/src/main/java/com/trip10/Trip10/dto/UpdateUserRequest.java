package com.trip10.Trip10.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRequest {
    private String username;

    @Email(message = "email must be valid")
    private String email;

    @Pattern(
            regexp = "^(\\+20|0)(10|11|12|15)\\d{8}$",
            message = "Phone number must be a valid Egyptian number"
    )
    private String phoneNumber;

    private String password;
}
