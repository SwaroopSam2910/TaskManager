package com.example.product_backend.projectBoards.Entity;

import com.example.product_backend.projectBoards.ENUM.BoardRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "boards")
public class Board {
    @Id
    private String id;

    private String name;

    private String description;

    private String createdBy; // userId

    private LocalDateTime createdAt;

}
