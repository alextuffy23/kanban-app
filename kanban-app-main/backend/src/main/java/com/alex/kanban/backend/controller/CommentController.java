package com.alex.kanban.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.alex.kanban.backend.dto.CommentRequest;
import com.alex.kanban.backend.dto.CommentResponse;
import com.alex.kanban.backend.service.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/cards/{cardId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long cardId) {
        return ResponseEntity.ok(commentService.getComments(cardId));
    }

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long cardId,
            @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.addComment(cardId, request));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long cardId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(
                commentService.updateComment(cardId, commentId, request));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long cardId,
            @PathVariable Long commentId) {
        commentService.deleteComment(cardId, commentId);
        return ResponseEntity.noContent().build();
    }
}
