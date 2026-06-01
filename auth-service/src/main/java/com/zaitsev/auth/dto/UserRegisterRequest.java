package com.zaitsev.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterRequest {

    @NotBlank(message = "First name required")
    private String firstName;

    @NotBlank(message = "Last name required")
    private String lastName;

    private String middleName;

    @NotBlank(message = "Birth date required")
    private String birthDate;

    @NotBlank(message = "Username required")
    private String username;

    @NotBlank(message = "Password required")
    @Size(min = 6, message = "Password too short")
    private String password;

    @NotBlank(message = "Phone required")
    private String phone;
}