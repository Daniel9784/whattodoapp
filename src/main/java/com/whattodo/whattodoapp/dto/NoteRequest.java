package com.whattodo.whattodoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.Pattern;

@Data
public class NoteRequest {

    @NotBlank(message = "Category must not be blank")
    private String category;

    @NotBlank(message = "Content must not be blank")
    private String content;

    @Pattern(regexp = "^$|\\d{4}-\\d{2}-\\d{2}", message = "Due date must be in format yyyy-MM-dd")
    @Size(min = 0)
    private String dueDate;

    @Pattern(regexp = "^$|\\d{2}:\\d{2}", message = "Due time must be in format HH:mm")
    @Size(min = 0)
    private String dueTime;
}