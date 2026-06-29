package com.example.product_backend.projectBoards.Controller;

import com.example.product_backend.projectBoards.DTO.*;
import com.example.product_backend.projectBoards.Service.boardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
public class boardController {

    @Autowired
    private boardService boardService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createBoard(@RequestBody createBoardRequest request) {
        return boardService.createBoard(request);
    }

    @GetMapping
    public List<BoardResponse> getMyBoards() {
        return boardService.getMyBoards();
    }

    @PostMapping("/{boardId}/members")
    public String addMember(@PathVariable String boardId,
                            @RequestBody addMemberRequest request) {
        return boardService.addMember(boardId, request);
    }

    @GetMapping("/{boardId}/members")
    public List<getAllBoardMembers> getMembers(@PathVariable String boardId) {
        return boardService.getBoardMembers(boardId);
    }

    @PutMapping("/{boardId}/members/{userId}")
    public String updateRole(@PathVariable String boardId,
                             @PathVariable String userId,
                             @RequestBody updateRoleRequest request) {
        return boardService.updateMemberRole(boardId, userId, request);
    }

    @DeleteMapping("/{boardId}/members/{userId}")
    public String removeMember(@PathVariable String boardId,
                               @PathVariable String userId) {
        return boardService.removeMember(boardId, userId);
    }
}
