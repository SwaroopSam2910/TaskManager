package com.example.product_backend.projectBoards.Service;

import com.example.product_backend.projectBoards.DTO.*;
import com.example.product_backend.projectBoards.ENUM.BoardRole;
import com.example.product_backend.projectBoards.Entity.Board;
import com.example.product_backend.projectBoards.Entity.BoardMembership;
import com.example.product_backend.projectBoards.Repository.BoardMembershipRepo;
import com.example.product_backend.projectBoards.Repository.BoardRepository;
import com.example.product_backend.user.Repository.userRepo;
import com.example.product_backend.user.entity.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class boardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardMembershipRepo boardMembershipRepo;

    @Autowired
    private userRepo userRepo;

    public String createBoard(createBoardRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String userId = auth.getName(); // comes from JWT

        // 🟢 1. Create Board
        Board board = new Board();
        board.setName(request.getName());
        board.setDescription(request.getDescription());
        board.setCreatedBy(userId);
        board.setCreatedAt(LocalDateTime.now());
        Board savedBoard = boardRepository.save(board);

        // 🟢 2. Create Membership (creator = ADMIN)
        BoardMembership membership = new BoardMembership();
        membership.setUserId(userId);
        membership.setBoardId(savedBoard.getId());
        membership.setRole(BoardRole.ADMIN);

        boardMembershipRepo.save(membership);

        return "Board created successfully";
    }

    public List<BoardResponse> getMyBoards() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        List<BoardMembership> memberships =
                boardMembershipRepo.findByUserId(userId);

        return memberships.stream()
                .map(membership -> {

                    Board board = boardRepository
                            .findById(membership.getBoardId())
                            .orElseThrow(() -> new RuntimeException("Board not found"));

                    return new BoardResponse(
                            board.getId(),
                            board.getName(),
                            board.getDescription(),
                            membership.getRole().name() // 🔥 IMPORTANT
                    );
                })
                .toList();
    }

    private BoardMembership getMembershipOrThrow(String userId, String boardId) {
        return boardMembershipRepo.findByUserIdAndBoardId(userId, boardId)
                .orElseThrow(() -> new RuntimeException("Access Denied"));
    }

    private void checkBoardAdmin(String userId, String boardId) {
        BoardMembership membership = getMembershipOrThrow(userId, boardId);

        if (membership.getRole() != BoardRole.ADMIN) {
            throw new RuntimeException("Only Board Admin can perform this action");
        }
    }

    public String addMember(String boardId, addMemberRequest request) {

        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        checkBoardAdmin(currentUserId, boardId);

        boolean exists = boardMembershipRepo
                .findByUserIdAndBoardId(request.getUserId(), boardId)
                .isPresent();

        if (exists) {
            throw new RuntimeException("User already a member");
        }

        BoardMembership membership = new BoardMembership();
        membership.setUserId(request.getUserId());
        membership.setBoardId(boardId);
        membership.setRole(BoardRole.valueOf(request.getRole()));

        boardMembershipRepo.save(membership);

        return "Member added successfully";
    }

    public List<getAllBoardMembers> getBoardMembers(String boardId) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        getMembershipOrThrow(userId, boardId);

        List<BoardMembership> memberships =boardMembershipRepo.findByBoardId(boardId);

        return memberships.stream()
                .map(membership -> {

                    user user = userRepo
                            .findById(membership.getUserId())
                            .orElseThrow(() -> new RuntimeException("Board not found"));

                    return new getAllBoardMembers(
                            user.getId(), // 🔥 IMPORTANT
                            user.getEmail(),
                            membership.getRole().name()
                    );
                })
                .toList();
    }

    public String updateMemberRole(String boardId, String targetUserId, updateRoleRequest request) {

        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 🔐 Only admin
        checkBoardAdmin(currentUserId, boardId);

        BoardMembership membership = boardMembershipRepo
                .findByUserIdAndBoardId(targetUserId, boardId)
                .orElseThrow(() -> new RuntimeException("User not part of board"));

        membership.setRole(BoardRole.valueOf(request.getRole()));

        boardMembershipRepo.save(membership);

        return "Role updated successfully";
    }

    public String removeMember(String boardId, String targetUserId) {

        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 🔐 Only admin
        checkBoardAdmin(currentUserId, boardId);

        BoardMembership membership = boardMembershipRepo
                .findByUserIdAndBoardId(targetUserId, boardId)
                .orElseThrow(() -> new RuntimeException("User not part of board"));

        boardMembershipRepo.delete(membership);

        return "Member removed successfully";
    }
}
