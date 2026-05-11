package com.example.product_backend.common;

import com.example.product_backend.user.entity.user;
import com.example.product_backend.user.ENUM.UserRole;
import com.example.product_backend.user.ENUM.UserStatus;
import com.example.product_backend.user.Repository.userRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(userRepo userRepository, PasswordEncoder passwordEncoder , MongoTemplate mongoTemplate) {
        return args -> {

            // check if admin exists
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {

                user admin = new user();
                admin.setEmail("admin@gmail.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(UserRole.ADMIN);
                admin.setStatus(UserStatus.ACTIVE);

                userRepository.save(admin);

                System.out.println("✅ Admin created");
            }
            //System.out.println(mongoTemplate.getDb().getName());
        };
    }
}