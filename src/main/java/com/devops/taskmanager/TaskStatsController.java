package com.devops.taskmanager;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TaskStatsController {

    @GetMapping("/api/tasks/stats")
    public Map<String, String> getTaskStats() {
        return Map.of(
                "feature", "Task Statistics",
                "description", "Provides task statistics"
        );
    }
}