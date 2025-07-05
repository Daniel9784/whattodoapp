package com.whattodo.whattodoapp.dto;

import lombok.Data;

@Data
public class ChangeEmailRequest {
    private String newEmail;
    private String currentPassword;
}

