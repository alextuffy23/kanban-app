package com.alex.kanban.backend.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "boards")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Board {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  // Auth0 user ID — "auth0|64abc123"
  // We store this instead of a FK to User table
  @Column(nullable = false)
  private String ownerAuth0Id;

  @Column(nullable = false)
  private boolean isPersonal = false;

  private String backgroundColour;

  private boolean archived = false;

  // ── Relationships ──
  @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  @ToString.Exclude
  private List<TaskList> lists = new ArrayList<>();

  @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  @ToString.Exclude
  private Set<BoardMembership> memberships = new HashSet<>();

  // ── Timestamps ──
  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

}
