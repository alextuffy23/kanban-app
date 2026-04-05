package com.alex.kanban.backend.service;


import java.security.Security;

import com.alex.kanban.backend.dto.BoardRequest;
import com.alex.kanban.backend.dto.BoardResponse;
import com.alex.kanban.backend.entity.Board;
import com.alex.kanban.backend.entity.BoardMembership;
import com.alex.kanban.backend.entity.BoardRole;
import com.alex.kanban.backend.repository.BoardRepository;
import com.alex.kanban.backend.service.BoardService;
import com.alex.kanban.backend.security.SecurityUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
  
  private final BoardRepository boardRepository;  

  public List<BoardResponse> getMyBoards() {
    String userId = SecurityUtils.getCurrentUserId();
    return boardRepository.findByOwnerAuth0IdAndArchivedFalse(userId)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
  }
  
  // Get single board
  public BoardResponse getBoard(Long id) {
    String userId = SecurityUtils.getCurrentUserId();
    Board board = boardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));

    boolean isOwner = board.getOwnerAuth0Id().equals(userId);
    boolean isMember = board.getMemberships().stream().anyMatch(m -> m.getAuth0UserId().equals(userId));

    if (!isOwner && !isMember) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
    }

    return toResponse(board);
  }

  //Create board
  @Transactional
  public BoardResponse createBoard(BoardRequest request) {
    String userId = SecurityUtils.getCurrentUserId();

    Board board = Board.builder()
              .title(request.getTitle())
              .description(request.getDescription())
              .isPersonal(request.isPersonal())
              .backgroundColour(request.getBackgroundColour())
              .ownerAuth0Id(userId)
              .build();

    //Add owner as owner membership
    BoardMembership ownerMembership = BoardMembership.builder()
              .board(board)
              .auth0UserId(userId)
              .role(BoardRole.OWNER)
              .build();
  
    board.getMemberships().add(ownerMembership);

    Board saved = boardRepository.save(board);              
    return toResponse(saved);

  }

  //Update board
  @Transactional
  public BoardResponse updateBoard(Long id,  BoardRequest request) {
    String userId = SecurityUtils.getCurrentUserId();

    Board board = boardRepository.findByIdAndOwnerAuth0Id(id, userId)
            .orElseThrow(() -> new ResponseStatusException(
                  HttpStatus.NOT_FOUND, "Board not found or access denied"));

    board.setTitle(request.getTitle());  
    board.setDescription(request.getDescription());
    board.setBackgroundColour(request.getBackgroundColour());
    
    return toResponse(boardRepository.save(board));     

  }

  //Archive Board
  @Transactional
  public void archiveBoard(Long id) {
    String userId = SecurityUtils.getCurrentUserId();

    Board board = boardRepository.findByIdAndOwnerAuth0Id(id, userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found or access denied"));

    board.setArchived(true);
    boardRepository.save(board);
  }

  // Map entity to response
  private BoardResponse toResponse(Board board) {
    return BoardResponse.builder()
              .id(board.getId())
              .title(board.getTitle())
              .description(board.getDescription())
              .isPersonal(board.isPersonal())
              .backgroundColour(board.getBackgroundColour())
              .archived(board.isArchived())
              .ownerAuth0Id(board.getOwnerAuth0Id())
              .createdAt(board.getCreatedAt())
              .updatedAt(board.getUpdatedAt())
              .build();
  }

}
