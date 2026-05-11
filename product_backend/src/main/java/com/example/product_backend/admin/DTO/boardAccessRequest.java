package com.example.product_backend.admin.DTO;

import com.example.product_backend.projectBoards.ENUM.BoardRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class boardAccessRequest {

    private String boardId;
    private BoardRole role;
}
