package com.alex.kanban.backend.entity;

import lombok.Data;
import java.time.LocalDateTime;
import lombok.Builder;

@Data
@Builder
public class MembershipResponse {
    private Long id;
    private String auth0UserId;
    private BoardRole role;
    private Long boardId;
    private LocalDateTime joinedAt;
}
