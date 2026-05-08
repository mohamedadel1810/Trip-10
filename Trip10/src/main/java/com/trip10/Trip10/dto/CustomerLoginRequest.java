package com.trip10.Trip10.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerLoginRequest {
    @NotBlank(message = "email required")
    @Email(message = "email must be valid")
    private String email;

    @NotBlank(message = "password required")
    private String password;
}
