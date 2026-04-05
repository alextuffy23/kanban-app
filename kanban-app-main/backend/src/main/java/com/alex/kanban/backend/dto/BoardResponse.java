package com.alex.kanban.backend.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardResponse {
  private Long id;
  private String title;
  private String description;
  private boolean isPersonal;
  private String backgroundColour;
  private boolean archived;
  private String ownerAuth0Id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
