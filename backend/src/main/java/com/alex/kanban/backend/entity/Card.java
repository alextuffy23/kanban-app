package com.alex.kanban.backend.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.List;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private Integer position;

  private LocalDateTime dueDate;
 
  private String labels;

  private boolean archived = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "task_list_id", nullable = false)
  @ToString.Exclude
  private TaskList taskList;


  @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  @ToString.Exclude
  private List<Comment> comments = new ArrayList<>();

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
