package com.example.product_backend.user.controller;

import com.example.product_backend.user.DTO.loginRequest;
import com.example.product_backend.user.DTO.RegisterRequest;
import com.example.product_backend.user.DTO.loginResponse;
import com.example.product_backend.user.Service.authService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class authController {

    @Autowired
    private authService authService;

    @PostMapping("/register")
    public ResponseEntity<String>registerUser(@RequestBody RegisterRequest request){
        try {
            String response = authService.registerUser(request);

            return ResponseEntity.ok().body(response);

        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public  ResponseEntity<?>loginUser(@RequestBody loginRequest request){

        try {
            loginResponse response = authService.loginUser(request);

            return ResponseEntity.ok().body(response);

        } catch (RuntimeException ex) {

            return ResponseEntity.status(401)
                    .body(ex.getMessage());
        }
    }

}
