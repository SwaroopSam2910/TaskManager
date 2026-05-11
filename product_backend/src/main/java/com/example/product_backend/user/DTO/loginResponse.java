package com.example.product_backend.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class loginResponse {

    String token;
    String role;
}
