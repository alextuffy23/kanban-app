package com.alex.kanban.backend.service;

import com.alex.kanban.backend.dto.CardResponse;
import com.alex.kanban.backend.dto.TaskListRequest;
import com.alex.kanban.backend.dto.TaskListResponse;
import com.alex.kanban.backend.entity.Board;
import com.alex.kanban.backend.entity.TaskList;
import com.alex.kanban.backend.repository.BoardRepository;
import com.alex.kanban.backend.repository.TaskListRepository;
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
public class TaskListService {
  private final TaskListRepository taskListRepository;
  private final BoardRepository boardRepository;

  // --Get lists for a board
  public List<TaskListResponse> getLists(Long boardId) {
    verifyBoardAccess(boardId);
    return taskListRepository.findByBoardIdOrderByPositionAsc(boardId)
    .stream()
    .map(this::toResponse)
    .collect(Collectors.toList());
  }

  //Create a list
  @Transactional
  public TaskListResponse createList(Long boardId, TaskListRequest request) {
    Board board = verifyBoardAccess(boardId);

    int position = request.getPosition() != null ? request.getPosition() : taskListRepository.countByBoardId(boardId);

    TaskList taskList = TaskList.builder()
    .title(request.getTitle())
    .position(position)
    .board(board)
    .build();

    return toResponse(taskListRepository.save(taskList));
  }

  //Update a List
  @Transactional
  public TaskListResponse updateList(Long boardId, Long listId, TaskListRequest request) {
    verifyBoardAccess(boardId);

    TaskList taskList = taskListRepository.findByIdAndBoardId(listId, boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "List not found"));

    taskList.setTitle(request.getTitle());
    if (request.getPosition() != null) {taskList.setPosition(request.getPosition());}

    return toResponse(taskListRepository.save(taskList));
  }

  //Delete a list
  @Transactional
  public void deleteList(Long boardId, Long listId) {
    verifyBoardAccess(boardId);

    TaskList taskList = taskListRepository.findByIdAndBoardId(listId, boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "List not found"));

    taskListRepository.delete(taskList);
  }

  private Board verifyBoardAccess(Long boardId)  {
    String userId = SecurityUtils.getCurrentUserId();

    Board board = boardRepository.findById(boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));

    boolean isOwner = board.getOwnerAuth0Id().equals(userId);
    boolean isMember = board.getMemberships().stream().anyMatch(m -> m.getAuth0UserId().equals(userId));

    if (!isOwner && isMember) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
    }

    return board;
  }

  // Map entity to response
  private TaskListResponse toResponse(TaskList taskList) {
    List<CardResponse> cards = taskList.getCards().stream()
      .filter(card -> !card.isArchived())
      .map(card -> CardResponse.builder()
              .id(card.getId())
              .title(card.getTitle())
              .description(card.getDescription())
              .position(card.getPosition())
              .dueDate(card.getDueDate())
              .labels(card.getLabels())
              .archived(card.isArchived())
              .taskListId(taskList.getId())
              .createdAt(card.getCreatedAt())
              .updatedAt(card.getUpdatedAt())
              .build())
      .collect(Collectors.toList());

    return TaskListResponse.builder()
        .id(taskList.getId())
        .title(taskList.getTitle())
        .position(taskList.getPosition())
        .boardId(taskList.getBoard().getId())
        .cards(cards)
        .createdAt(taskList.getCreatedAt())
        .updatedAt(taskList.getUpdatedAt())
        .build();
  }
}
