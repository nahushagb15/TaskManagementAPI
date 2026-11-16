package com.devops.taskmanager;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TaskStatsController {

    private final TaskRepository taskRepository;

    public TaskStatsController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/api/tasks/stats")
    public Map<String, Object> getTaskStats() {

        List<Task> tasks = taskRepository.findAll();

        long totalTasks = tasks.size();
        long completedTasks = tasks.stream()
                .filter(Task::isCompleted)
                .count();

        long pendingTasks = totalTasks - completedTasks;

        return Map.of(
                "totalTasks", totalTasks,
                "completedTasks", completedTasks,
                "pendingTasks", pendingTasks
        );
    }
}
