package com.trip10.Trip10.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminRequest {

    private String name;

    @NotBlank
    @Email(message = "email must be valid...")
    private String email;

    private String password;


    private int permissionId;
}
