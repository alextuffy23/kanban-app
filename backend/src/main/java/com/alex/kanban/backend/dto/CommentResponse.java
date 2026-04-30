package com.alex.kanban.backend.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponse {
    private Long id;
    private String test;
    private String auth0UserId;
    private Long cardId;
    private LocalDateTime createdAt;

}
