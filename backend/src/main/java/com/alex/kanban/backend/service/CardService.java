package com.alex.kanban.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.alex.kanban.backend.dto.CardRequest;
import com.alex.kanban.backend.dto.CardResponse;
import com.alex.kanban.backend.entity.Card;
import com.alex.kanban.backend.entity.TaskList;
import com.alex.kanban.backend.entity.Board;
import com.alex.kanban.backend.repository.BoardRepository;
import com.alex.kanban.backend.repository.CardRepository;
import com.alex.kanban.backend.repository.TaskListRepository;
import com.alex.kanban.backend.security.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
  
  private final TaskListRepository taskListRepository;
  private final BoardRepository boardRepository;
  private final CardRepository cardRepository;

  //Get lists for a board
  public List<CardResponse> getCards(Long boardId, Long listId) {
    verifyAccess(boardId, listId);
    return cardRepository.findByTaskListIdOrderByPosition(listId)
    .stream()
    .filter(card -> !card.isArchived())
    .map(this::toResponse)
    .collect(Collectors.toList());
  }

  // create a card
  @Transactional
  public CardResponse createCard(Long boardId, Long listId, CardRequest request) {
    TaskList taskList = verifyAccess(boardId, listId);

    int position = request.getPosition() != null ? request.getPosition() : cardRepository.countByTaskListId(listId);

    Card card = Card.builder()
    .title(request.getTitle())
    .description(request.getDescription())
    .position(position)
    .dueDate(request.getDueDate())
    .labels(request.getLabels())
    .taskList(taskList)
    .build();

    return toResponse(cardRepository.save(card));
  }
  // update a card
  @Transactional
  public CardResponse updateCard(Long boardId, Long listId, Long cardId, CardRequest request) {
    verifyAccess(boardId, listId);

    Card card = cardRepository.findByIdAndTaskListId(cardId, listId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found"));

    card.setTitle(request.getTitle());
    card.setDescription(request.getDescription());
    card.setDueDate(request.getDueDate());
    card.setLabels(request.getLabels());
    if (request.getPosition()!= null) {
      card.setPosition(request.getPosition());
    }

    return toResponse(cardRepository.save(card));
  }
  // Archive a card
  @Transactional
  public void archiveCard(Long boardId, Long listId, Long cardId) {
    verifyAccess(boardId, listId);

    Card card = cardRepository.findByIdAndTaskListId(cardId, listId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found"));

    card.setArchived(true);
    cardRepository.save(card);
  }

  // Move card to different list
  @Transactional
  public CardResponse moveCard(Long boardId, Long listId, Long cardId, Long targetListId) {
    verifyAccess(boardId, listId);


    Card card = cardRepository.findByIdAndTaskListId(cardId, listId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found"));

    TaskList targetList = taskListRepository.findByIdAndBoardId(targetListId, boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "List not found"));

    card.setTaskList(targetList);
    card.setPosition(cardRepository.countByTaskListId(targetListId));
    return toResponse(cardRepository.save(card));
  }

  private TaskList verifyAccess(Long boardId, Long listId)  {
    String userId = SecurityUtils.getCurrentUserId();

    var board = boardRepository.findById(boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));

    boolean hasAccess = board.getOwnerAuth0Id().equals(userId) || board.getMemberships().stream().anyMatch(m -> m.getAuth0UserId().equals(userId));

    if (!hasAccess) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
    }

    return taskListRepository.findByIdAndBoardId(listId, boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
  }

  private CardResponse toResponse(Card card) {
    return CardResponse.builder()
    .id(card.getId())
    .title(card.getTitle())
    .description(card.getDescription())
    .position(card.getPosition())
    .dueDate(card.getDueDate())
    .labels(card.getLabels())
    .archived(card.isArchived())
    .taskListId(card.getTaskList().getId())
    .createdAt(card.getCreatedAt())
    .updatedAt(card.getUpdatedAt())
    .build();
  }
}
