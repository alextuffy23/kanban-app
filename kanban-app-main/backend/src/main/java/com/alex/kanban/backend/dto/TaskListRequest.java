package com.alex.kanban.backend.dto;
import lombok.Data;

@Data
public class TaskListRequest {
  private String title;
  private Integer position;
}
