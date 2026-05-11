package com.example.product_backend.projectBoards.Entity;

import com.example.product_backend.projectBoards.ENUM.BoardRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "board_memberships")
public class BoardMembership {

    @Id
    private String id;

    private String userId;

    private String boardId;

    private BoardRole role;

}
