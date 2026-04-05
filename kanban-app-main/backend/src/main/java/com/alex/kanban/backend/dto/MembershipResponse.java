package com.alex.kanban.backend.dto;

import java.time.LocalDateTime;
import com.alex.kanban.backend.entity.BoardRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MembershipResponse {
    private Long id;
    private String auth0UserId;
    private BoardRole role;
    private Long boardId;
    private LocalDateTime joinedAt;  
}
