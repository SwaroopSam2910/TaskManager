package com.example.product_backend.TaskManager.Controller;

import com.example.product_backend.TaskManager.DTO.*;
import com.example.product_backend.TaskManager.Entities.Task;
import com.example.product_backend.TaskManager.Service.taskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task")
public class taskController {

    @Autowired
    private taskService taskService;

    @PostMapping
    public createTaskResponse createTask(@RequestBody createTaskRequest request) {
        return taskService.createTask(request);
    }

    @GetMapping("/board/{boardId}")
    public Map<String, List<createTaskResponse>> getTasks(@PathVariable String boardId) {
        return taskService.getTasksByBoard(boardId);
    }

    @PutMapping("/{taskId}")
    public String updateTask(@PathVariable String taskId,
                             @RequestBody updateTaskRequest request) {
        return taskService.updateTask(taskId, request);
    }

    @PatchMapping("/{taskId}/status")
    public String updateStatus(
            @PathVariable String taskId,
            @RequestParam String status
    ) {
        return taskService.updateStatus(taskId, status);
    }

    @PutMapping("/{taskId}/assign")
    public String assignTask(@PathVariable String taskId,
                             @RequestBody assignTaskRequest request) {
        return taskService.assignTask(taskId, request);
    }

    @DeleteMapping("/{taskId}")
    public String deleteTask(@PathVariable String taskId) {
        return taskService.deleteTask(taskId);
    }
}
