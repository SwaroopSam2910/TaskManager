package com.example.product_backend.TaskManager.DTO;

import com.example.product_backend.TaskManager.ENUM.TaskPriority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class createTaskRequest {

    private String title;
    private String description;
    private String boardId;
    private String assignedToId;
    private TaskPriority priority;
}
