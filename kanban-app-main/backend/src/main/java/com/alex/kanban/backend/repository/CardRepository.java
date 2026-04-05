package com.alex.kanban.backend.repository;

import com.alex.kanban.backend.entity.Card;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

  List<Card> findByTaskListIdOrderByPosition(Long taskListId);

  Optional<Card> findByIdAndTaskListId(Long id, Long taskListId);

  int countByTaskListId(Long taskListId);
}
