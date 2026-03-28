package com.alex.kanban.backend.service;

import com.alex.kanban.backend.dto.CommentRequest;
import com.alex.kanban.backend.dto.CommentResponse;
import com.alex.kanban.backend.entity.Card;
import com.alex.kanban.backend.entity.Comment;
import com.alex.kanban.backend.repository.CardRepository;
import com.alex.kanban.backend.repository.CommentRepository;
import com.alex.kanban.backend.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    public List<CommentResponse> getComments(Long cardId) {
        verifyCardExists(cardId);
    }

    private Card verifyCardExists(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found"));
    }

    private CommentReponse toResponse(Comment comment) {
        return Comment.builder()
        .id(comment.getId())
        .text(comment.getText())
        .auth0UserId(comment.getAuth0UserId())
        .cardId(comment.getCard().getId())
        .createdAt(comment.getCreatedAt())
        .build();
    }
}
