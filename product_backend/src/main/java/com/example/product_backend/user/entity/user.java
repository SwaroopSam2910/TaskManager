package com.example.product_backend.user.entity;

import com.example.product_backend.user.ENUM.UserRole;
import com.example.product_backend.user.ENUM.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class user {

    @Id
    private String id;

    private String email;
    private String password;

    private UserStatus status;

    private UserRole role;
}
