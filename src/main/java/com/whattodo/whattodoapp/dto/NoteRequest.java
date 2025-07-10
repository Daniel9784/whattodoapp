package com.whattodo.whattodoapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NoteRequest {

    @NotBlank(message = "Category must not be blank")
    private String category;

    @NotBlank(message = "Content must not be blank")
    private String content;
}
