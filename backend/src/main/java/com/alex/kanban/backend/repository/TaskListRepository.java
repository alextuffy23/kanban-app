package com.alex.kanban.backend.repository;

import com.alex.kanban.backend.entity.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, Long>{
  List<TaskList> findByBoardIdOrderByPositionAsc(Long boardId);

  Optional<TaskList> findByIdAndBoardId(Long id, Long boardId);
  int countByBoardId(Long boardId);
}
