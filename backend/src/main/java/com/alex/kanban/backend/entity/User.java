package com.alex.kanban.backend.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity  //tells Hibernate/JPA this class maps to a db table. 
@Table(name = "users") 
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 

public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique= true, nullable = false)
  private String username;

  @Column(unique= true,  nullable = false)
  private String email;

  @Column(nullable = false)
  private String password; 

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
    name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id")
  )
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private Set<Role> roles = new HashSet<>();

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;
}


