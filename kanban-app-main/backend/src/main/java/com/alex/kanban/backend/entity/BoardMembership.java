package com.alex.kanban.backend.entity;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "board_memberships",
      uniqueConstraints = @UniqueConstraint(
        columnNames = {"board_id", "auth0_user_id"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardMembership {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id", nullable = false)
  @ToString.Exclude
  private Board board;

  @Column(name = "auth0UserId", nullable = false)
  private String auth0UserId;

  @Enumerated(EnumType.STRING)
  @Column(nullable=false)
  private BoardRole role;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime joinedAt;
}
