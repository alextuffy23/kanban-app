package com.alex.kanban.backend.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskListResponse {
  private Long id;
  private String title;
  private Integer position;
  private Long boardId;
  private List<CardResponse> cards;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
