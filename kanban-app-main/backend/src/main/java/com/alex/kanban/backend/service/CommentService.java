package com.alex.kanban.backend.service;

import com.alex.kanban.backend.dto.CommentRequest;
import com.alex.kanban.backend.dto.CommentResponse;
import com.alex.kanban.backend.entity.Card;
import com.alex.kanban.backend.entity.Comment;
import com.alex.kanban.backend.repository.CardRepository;
import com.alex.kanban.backend.repository.CommentRepository;
import com.alex.kanban.backend.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    public List<CommentResponse> getComments(Long cardId) {
        verifyCardExists(cardId);
        return commentRepository.findByCardIdOrderByCreatedAtDesc(cardId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    //Add comment
    @Transactional
    public CommentResponse addComment(Long cardId, CommentRequest request) {
        Card card = verifyCardExists(cardId);
        String userId = SecurityUtils.getCurrentUserId();

        Comment comment = Comment.builder()
        .text(request.getText())
        .auth0UserId(userId)
        .card(card)
        .build();

        return toResponse(commentRepository.save(comment));
    }

    //Update Comment
    @Transactional
    public CommentResponse updateComment(Long cardId, Long commentId, CommentRequest request) {
        verifyCardExists(cardId);
        String userId = SecurityUtils.getCurrentUserId();

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        //Only the author can edit their comment
        if (!comment.getAuth0UserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only edit your own comments");
        }

        comment.setText(request.getText());
        return toResponse(commentRepository.save(comment));
    }

    //Delete comment
    @Transactional
    public void deleteComment(Long cardId, Long commentId) {
        verifyCardExists(cardId);
        String userId = SecurityUtils.getCurrentUserId();

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (!comment.getAuth0UserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own comments");
        }
        commentRepository.delete(comment);
    }

    //Verify Card exists
    private Card verifyCardExists(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found"));
    }

    private CommentResponse toResponse(Comment comment) {
        return CommentResponse.builder()
        .id(comment.getId())
        .text(comment.getText())
        .auth0UserId(comment.getAuth0UserId())
        .cardId(comment.getId())
        .createdAt(comment.getCreatedAt())
        .build();
    }
}
