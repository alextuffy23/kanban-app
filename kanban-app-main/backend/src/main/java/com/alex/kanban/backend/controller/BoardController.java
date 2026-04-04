package com.alex.kanban.backend.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.kanban.backend.dto.BoardRequest;
import com.alex.kanban.backend.dto.BoardResponse;
import com.alex.kanban.backend.service.BoardService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;

  @GetMapping
  public ResponseEntity<List<BoardResponse>> getMyBoards() {
    return ResponseEntity.ok(boardService.getMyBoards());
  }

  @GetMapping("/{id}")
  public ResponseEntity<BoardResponse> getBoard(@PathVariable Long id) {
      return ResponseEntity.ok(boardService.getBoard(id));
  }

  @PostMapping
  public ResponseEntity<BoardResponse> createBoard(@RequestBody BoardRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(boardService.createBoard(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<BoardResponse> updateBoard (@PathVariable Long id, @RequestBody BoardRequest request) {
            
      return ResponseEntity.ok(boardService.updateBoard(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> archiveBoard(@PathVariable Long id) {
    boardService.archiveBoard(id);
    return ResponseEntity.noContent().build();
  }
}
