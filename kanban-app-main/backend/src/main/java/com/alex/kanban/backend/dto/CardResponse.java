package com.alex.kanban.backend.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardResponse {
  private Long id;
  private String title;
  private String description;
  private Integer position;
  private LocalDateTime dueDate;
  private String labels;
  private boolean archived;
  private Long taskListId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
