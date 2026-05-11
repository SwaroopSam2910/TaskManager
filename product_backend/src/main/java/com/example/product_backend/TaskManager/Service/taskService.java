package com.example.product_backend.TaskManager.Service;

import com.example.product_backend.TaskManager.DTO.*;
import com.example.product_backend.TaskManager.ENUM.TaskStatus;
import com.example.product_backend.TaskManager.Entities.Task;
import com.example.product_backend.TaskManager.Repository.taskRepository;
import com.example.product_backend.projectBoards.ENUM.BoardRole;
import com.example.product_backend.projectBoards.Entity.BoardMembership;
import com.example.product_backend.projectBoards.Repository.BoardMembershipRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class taskService {

        @Autowired
        private taskRepository taskRepository;

        @Autowired
        private BoardMembershipRepo membershipRepository;

        private BoardMembership getMembership(String userId, String boardId) {
            return membershipRepository
                    .findByUserIdAndBoardId(userId, boardId)
                    .orElseThrow(() -> new RuntimeException("Access Denied"));
        }

        private void checkAdmin(BoardMembership membership) {
            if (!(membership.getRole() == BoardRole.ADMIN)) {
                throw new RuntimeException("Only admin allowed");
            }
        }

    private void checkCanEdit(BoardMembership membership) {
        if (membership.getRole() == BoardRole.VIEWER ||
                membership.getRole() == BoardRole.MEMBER) {
            throw new RuntimeException("Access Denied");
        }
    }

    private void checkCanEditNoViewer(BoardMembership membership) {
        if (membership.getRole() == BoardRole.VIEWER) {
            throw new RuntimeException("Access Denied");
        }
    }

    public createTaskResponse createTask(createTaskRequest request) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        BoardMembership membership = getMembership(userId, request.getBoardId());
        checkCanEdit(membership);

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setBoardId(request.getBoardId());
        task.setAssignedToId(request.getAssignedToId());
        task.setCreatedBy(userId);
        task.setPriority(request.getPriority());
        task.setStatus(TaskStatus.TODO);
        task.setCreatedAt(LocalDate.now());

        Task saved = taskRepository.save(task);

        return new createTaskResponse(saved); // 🔥 return object
    }

    public Map<String, List<createTaskResponse>> getTasksByBoard(String boardId) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        getMembership(userId, boardId);

        List<Task> tasks = taskRepository.findByBoardId(boardId);

        Map<String, List<createTaskResponse>> grouped = new HashMap<>();

        grouped.put("TODO", new ArrayList<>());
        grouped.put("PROGRESS", new ArrayList<>());
        grouped.put("DONE", new ArrayList<>());

        for (Task task : tasks) {
            grouped.get(task.getStatus().name())
                    .add(new createTaskResponse(task)); // 🔥 important
        }

        return grouped;
    }

        public String updateTask(String taskId, updateTaskRequest request) {

            String userId = SecurityContextHolder.getContext().getAuthentication().getName();

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new RuntimeException("Task not found"));

            BoardMembership membership = getMembership(userId, task.getBoardId());

            checkCanEdit(membership);

            task.setTitle(request.getTitle());
            task.setDescription(request.getDescription());

            taskRepository.save(task);

            return "Task updated successfully";
        }

        public String updateStatus(String taskId, String status) {

            String userId = SecurityContextHolder.getContext().getAuthentication().getName();

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new RuntimeException("Task not found"));

            BoardMembership membership = getMembership(userId, task.getBoardId());

            checkCanEditNoViewer(membership);

            try {
                TaskStatus newStatus = TaskStatus.valueOf(status.toUpperCase());
                task.setStatus(newStatus);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid status: " + status);
            }

            taskRepository.save(task);

            return "Task status updated";
        }

        public String assignTask(String taskId, assignTaskRequest request) {

            String userId = SecurityContextHolder.getContext().getAuthentication().getName();

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new RuntimeException("Task not found"));

            BoardMembership membership = getMembership(userId, task.getBoardId());

            checkCanEdit(membership);

            task.setAssignedToId(request.getUserId());

            taskRepository.save(task);

            return "Task assigned";
        }

        public String deleteTask(String taskId) {

            String userId = SecurityContextHolder.getContext().getAuthentication().getName();

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new RuntimeException("Task not found"));

            BoardMembership membership = getMembership(userId, task.getBoardId());

            checkAdmin(membership);
            taskRepository.deleteById(taskId);

            return "Task deleted";
        }
    }
