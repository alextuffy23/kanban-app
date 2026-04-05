package com.alex.kanban.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.alex.kanban.backend.entity.Board;
import com.alex.kanban.backend.entity.BoardMembership;

public interface BoardMembershipRepository extends JpaRepository<BoardMembership, Long>{
    List<BoardMembership> findByBoardId(Long board);
    Optional<BoardMembership> findByBoardIdAndAuth0UserId(Long boardId, String auth0UserId);
    boolean existsByBoardIdAndAuth0UserId(Long boardId, String auth0UserId);
    void deleteByBoardIdAndAuth0UserId(Long boardId, String auth0UserId);
}
