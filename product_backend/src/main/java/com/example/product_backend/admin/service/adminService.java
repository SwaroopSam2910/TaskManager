package com.example.product_backend.admin.service;

import com.example.product_backend.admin.DTO.approveUserRequest;
import com.example.product_backend.admin.DTO.boardAccessRequest;
import com.example.product_backend.admin.DTO.userResponse;
import com.example.product_backend.projectBoards.Entity.BoardMembership;
import com.example.product_backend.projectBoards.Repository.BoardMembershipRepo;
import com.example.product_backend.user.ENUM.UserStatus;
import com.example.product_backend.user.Repository.userRepo;
import com.example.product_backend.user.entity.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class adminService {

    @Autowired
    private userRepo userRepository;

    @Autowired
    private BoardMembershipRepo boardMembershipRepo;

    public String approveUser(approveUserRequest request) {

        user user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ 1. Activate user
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        // ✅ 2. Assign boards
        for (boardAccessRequest board : request.getBoards()) {

            if (!boardMembershipRepo
                    .existsByUserIdAndBoardId(user.getId(), board.getBoardId())) {

                BoardMembership membership = new BoardMembership();

                membership.setUserId(user.getId());
                membership.setBoardId(board.getBoardId());
                membership.setRole(board.getRole());

                boardMembershipRepo.save(membership);
            }
        }

        return "User approved and board access assigned";
    }

    public List<userResponse> getUsers(UserStatus status) {

        List<user> users;

        if (status != null) {
            users = userRepository.findByStatus(status);
        } else {
            users = userRepository.findAll();
        }

        return users.stream()
                .map(user -> new userResponse(
                        user.getId(),
                        user.getEmail()
                ))
                .toList();
    }

    public String rejectUser(String userId) {

        user user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(UserStatus.REJECTED);
        userRepository.save(user);

        return "User rejected successfully";
    }
}
