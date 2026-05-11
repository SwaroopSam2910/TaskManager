package com.example.product_backend.user.Repository;

import com.example.product_backend.user.ENUM.UserStatus;
import com.example.product_backend.user.entity.user;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface userRepo extends MongoRepository<user,String> {

    Optional<user>findByEmail(String email);
    List<user>findByStatus(UserStatus status);
}
