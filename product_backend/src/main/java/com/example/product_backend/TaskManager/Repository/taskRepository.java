package com.example.product_backend.TaskManager.Repository;

import com.example.product_backend.TaskManager.ENUM.TaskStatus;
import com.example.product_backend.TaskManager.Entities.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface taskRepository extends MongoRepository<Task,String> {

    List<Task>findByBoardId(String boardId);

    List<Task> findByBoardIdAndStatus(String boardId, TaskStatus status);

    List<Task> findByAssignedToId(String userId);
}
