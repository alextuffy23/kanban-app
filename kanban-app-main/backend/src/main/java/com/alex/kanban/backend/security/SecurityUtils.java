package com.alex.kanban.backend.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;


public class SecurityUtils {
  
  //Get Auth0 user ID from token
  public static String getCurrentUserId() {
    Jwt jwt = (Jwt) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
    // Debug - print all claims to see what's available
    System.out.println("JWT Claims: " + jwt.getClaims());
    System.out.println("Subject: " + jwt.getSubject());
    
    return jwt.getClaimAsString("sub"); // explicitly get 'sub' claim
  }

  public static String getCurrentEmail() {
    Jwt jwt = (Jwt) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
    return jwt.getClaimAsString("email");
  }
}
