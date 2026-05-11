package com.example.product_backend.TaskManager.Entities;

import com.example.product_backend.TaskManager.ENUM.TaskPriority;
import com.example.product_backend.TaskManager.ENUM.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    private String taskId;

    private String title;
    private String description;

    private String boardId;

    private String assignedToId;
    private String createdBy;

    private TaskStatus status;
    private TaskPriority priority;

    private LocalDate createdAt;

}
