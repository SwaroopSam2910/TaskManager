package com.example.product_backend.admin.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class approveUserRequest {

    private String userId;
    private List<boardAccessRequest> boards;
}
