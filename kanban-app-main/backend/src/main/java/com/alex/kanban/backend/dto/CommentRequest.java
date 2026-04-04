package com.alex.kanban.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank(message = "Comment cannot be blank")
    @Size(max =2000, message = "Comment cannot exceed 2000 characters")
    private String text;
}
