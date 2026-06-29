package com.example.product_backend.projectBoards.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponse {

    private String id;
    private String name;
    private String description;
    private String role;

}
