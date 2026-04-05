package com.alex.kanban.backend.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CardRequest {
  private String title;
  private String description;
  private Integer position;
  private LocalDateTime dueDate;
  private String labels;
}
