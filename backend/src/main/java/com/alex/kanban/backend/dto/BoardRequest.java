package com.alex.kanban.backend.dto;

import lombok.Data;

@Data
public class BoardRequest {
  private String title;
  private String description;
  private boolean isPersonal;
  private String backgroundColour;
}
