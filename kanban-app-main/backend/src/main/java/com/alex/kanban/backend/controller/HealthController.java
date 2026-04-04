package com.alex.kanban.backend.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class HealthController {

    @GetMapping("/")
    public String welcome() {
        return "Kanban Backend is running! Server time: " + java.time.LocalDateTime.now();
    }

    @GetMapping("/health")
    public String health() {
        return "OK - " + java.time.LocalDateTime.now();
    }
}