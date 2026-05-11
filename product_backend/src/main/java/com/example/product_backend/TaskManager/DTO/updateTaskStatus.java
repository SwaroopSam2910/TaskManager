package com.example.product_backend.TaskManager.DTO;

import com.example.product_backend.TaskManager.ENUM.TaskStatus;
import lombok.Data;

@Data
public class updateTaskStatus {

    private TaskStatus status;
}
