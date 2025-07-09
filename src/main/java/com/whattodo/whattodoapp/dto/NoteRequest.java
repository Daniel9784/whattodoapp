package com.whattodo.whattodoapp.dto;

import lombok.Data;

@Data
public class NoteRequest {
    private String category;
    private String content;
}
