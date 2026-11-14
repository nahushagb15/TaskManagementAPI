package com.devops.taskmanager;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final Map<Integer, Task> tasks = new LinkedHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    // Add a new task
    @PostMapping
    public ResponseEntity<Task> addTask(@RequestBody Task task) {

        int id = idCounter.getAndIncrement();

        Task newTask = new Task(
                id,
                task.getTitle(),
                task.getDescription(),
                task.isCompleted()
        );

        tasks.put(id, newTask);

        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    // Get all tasks
    @GetMapping
    public Collection<Task> getAllTasks() {
        return tasks.values();
    }

    // Get a task by ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable int id) {

        Task task = tasks.get(id);

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(task);
    }
}