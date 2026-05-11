package com.example.product_backend.TaskManager.DTO;

import com.example.product_backend.TaskManager.Entities.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class createTaskResponse {

    private String id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String assignedToId;
    private String createdBy;
    private LocalDate createdAt;

    public createTaskResponse(Task task) {
        this.id = task.getTaskId(); //
        this.title=task.getTitle();
        this.description = task.getDescription();
        this.assignedToId = task.getAssignedToId();
        this.priority = task.getPriority().name();
        this.status = task.getStatus().name();
        this.createdBy = task.getCreatedBy();
        this.createdAt= task.getCreatedAt();
    }

}
