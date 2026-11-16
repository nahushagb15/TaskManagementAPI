package com.devops.taskmanager;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Add a new task
    @PostMapping
    public ResponseEntity<Task> addTask(@RequestBody Task task) {

        Task newTask = new Task(
                null,
                task.getTitle(),
                task.getDescription(),
                task.isCompleted()
        );

        Task savedTask = taskRepository.save(newTask);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    // Get all tasks
    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Get a task by ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable int id) {

        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
