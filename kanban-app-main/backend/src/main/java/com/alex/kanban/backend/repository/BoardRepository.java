package com.alex.kanban.backend.repository;

import com.alex.kanban.backend.entity.Board;
import  java.util.List;
import  java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
  
  //Get all boards owned by a user
  List<Board> findByOwnerAuth0IdAndArchivedFalse (String ownerAuth0Id);

  //Get all boards where user is a member
  List<Board> findByMemberships_Auth0UserIdAndArchivedFalse(String auth0UserId);
  
  //Find specific board by id and owner
  Optional<Board> findByIdAndOwnerAuth0Id(Long id, String owernerAuth0Id);
}
