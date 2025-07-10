package com.whattodo.whattodoapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangeEmailRequest {

    @NotBlank
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
    private String newEmail;

    @NotBlank
    private String currentPassword;
}

