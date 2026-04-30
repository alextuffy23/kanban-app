package com.alex.kanban.backend.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.alex.kanban.backend.dto.CardRequest;
import com.alex.kanban.backend.dto.CardResponse;
import com.alex.kanban.backend.service.CardService;




@RestController
@RequestMapping("/api/boards/{boardId}/lists/{listId}/cards")
@RequiredArgsConstructor
public class CardController {
  private final CardService cardService;

  @GetMapping
  public ResponseEntity <List<CardResponse>> getCards(
      @PathVariable Long boardId,
      @PathVariable Long listId) {
    return ResponseEntity.ok(cardService.getCards(boardId, listId));
  }

  @PostMapping
  public ResponseEntity<CardResponse>createCard(
    @PathVariable Long boardId,
    @PathVariable Long listId,
    @RequestBody CardRequest request) {
      
      return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(boardId, listId, request));
  }
  
  @PutMapping("/{cardId}")
  public ResponseEntity<CardResponse> updateCard(
    @PathVariable Long boardId, 
    @PathVariable Long listId,
    @PathVariable Long cardId,
    @RequestBody CardRequest request) {
      return ResponseEntity.ok(cardService.updateCard(boardId, listId, cardId, request));
  }

  @DeleteMapping("/{cardId}")
  public ResponseEntity<Void> archiveCard(
      @PathVariable Long boardId,
      @PathVariable Long listId,
      @PathVariable Long cardId) {

    cardService.archiveCard(boardId, listId, cardId);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{cardId}/move/{targetListId}")
  public ResponseEntity<CardResponse>moveCard(
    @PathVariable Long boardId, 
    @PathVariable Long listId,
    @PathVariable Long cardId,
    @PathVariable Long targetListId) {
  
      
      return ResponseEntity.ok(cardService.moveCard(boardId, listId, cardId, targetListId));
  }
}
