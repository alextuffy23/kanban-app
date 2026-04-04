package com.alex.kanban.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alex.kanban.backend.dto.TaskListRequest;
import com.alex.kanban.backend.dto.TaskListResponse;
import com.alex.kanban.backend.service.TaskListService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/boards/{boardId}/lists")
@RequiredArgsConstructor
public class TaskListController {
  private final TaskListService taskListService;

  @GetMapping()
  public ResponseEntity<List<TaskListResponse>> getLists(@PathVariable Long boardId) {
      return ResponseEntity.ok(taskListService.getLists(boardId));
  }
  
  @PostMapping()
  public ResponseEntity<TaskListResponse> createList (
        @PathVariable Long boardId,
        @RequestBody TaskListRequest request) {

    return ResponseEntity.status(HttpStatus.CREATED).body(taskListService.createList(boardId, request));
  }

  
  @PutMapping("/{listId}")
  public ResponseEntity<TaskListResponse> updateList(@PathVariable Long boardId, @PathVariable Long listId, @RequestBody TaskListRequest request) {
      
      return ResponseEntity.ok(taskListService.updateList(boardId, listId, request));
  }
  
  @DeleteMapping("/{listId}") 
    public ResponseEntity<Void> deleteList(
          @PathVariable Long boardId,
          @PathVariable Long listId) { 
        taskListService.deleteList(boardId, listId);
        return ResponseEntity.noContent().build();    
  }
}
