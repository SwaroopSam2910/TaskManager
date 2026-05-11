package com.example.product_backend.projectBoards.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class getAllBoardMembers {

    private String userId;
    private String email;
    private String role;

}
