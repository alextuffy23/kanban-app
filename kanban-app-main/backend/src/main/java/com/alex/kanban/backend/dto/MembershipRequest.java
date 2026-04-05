package com.alex.kanban.backend.dto;

import com.alex.kanban.backend.entity.BoardRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class MembershipRequest {

    @NotBlank(message = "User ID cannot be blank")
    private String auth0UserId;

    @NotNull(message = "Role cannot be null")
    private BoardRole role;    
}
