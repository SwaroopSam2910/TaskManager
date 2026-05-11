package com.example.product_backend.admin.controller;

import com.example.product_backend.admin.DTO.approveUserRequest;
import com.example.product_backend.admin.DTO.userResponse;
import com.example.product_backend.admin.service.adminService;
import com.example.product_backend.user.ENUM.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class adminController {

    @Autowired
    private adminService adminService;

    @PostMapping("/approve")
    public String approveUser(@RequestBody approveUserRequest request) {
        return adminService.approveUser(request);
    }

    @GetMapping("/users")
    public List<userResponse> getUsers(@RequestParam(required = false) String status) {

        UserStatus userStatus = null;

        if (status != null) {
            userStatus = UserStatus.valueOf(status.toUpperCase());
        }

        return adminService.getUsers(userStatus);
    }

    @DeleteMapping("/reject/{userId}")
    public String rejectUser(@PathVariable String userId) {
        return adminService.rejectUser(userId);
    }
}
