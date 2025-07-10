package com.whattodo.whattodoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowNotesRequest {
    private Long id;
    private String category;
    private String content;
    private String createdAt;
}
