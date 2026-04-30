package com.alex.kanban.backend.controller;

import com.alex.kanban.backend.dto.MembershipResponse;
import com.alex.kanban.backend.dto.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards/{boardId}/members")
@RequiredArgsConstructor
public class BoardMembershipController {

    private final BoardMembershipService membershipService;

    @GetMapping
    public ResponseEntity<List<MembershipResponse>> getMembers(
            @PathVariable Long boardId) {
        return ResponseEntity.ok(membershipService.getMembers(boardId));
    }

    @PostMapping
    public ResponseEntity<MembershipResponse> addMember(
            @PathVariable Long boardId,
            @Valid @RequestBody MembershipRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(membershipService.addMember(boardId, request));
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MembershipResponse> updateMemberRole(
            @PathVariable Long boardId,
            @PathVariable Long memberId,
            @Valid @RequestBody MembershipRequest request) {
        return ResponseEntity.ok(
                membershipService.updateMemberRole(boardId, memberId, request));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long boardId,
            @PathVariable Long memberId) {
        membershipService.removeMember(boardId, memberId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/leave")
    public ResponseEntity<Void> leaveBoard(@PathVariable Long boardId) {
        membershipService.leaveBoard(boardId);
        return ResponseEntity.noContent().build();
    }
}