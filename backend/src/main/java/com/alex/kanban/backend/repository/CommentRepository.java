package com.alex.kanban.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alex.kanban.backend.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment>findByCardIdOrderByCreatedAtDesc(Long cardId);
    void deleteByCardId(Long cardId);
}
