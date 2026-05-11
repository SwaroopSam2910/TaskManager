package com.example.product_backend.projectBoards.Repository;

import com.example.product_backend.projectBoards.Entity.BoardMembership;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardMembershipRepo extends MongoRepository<BoardMembership,String> {

    List<BoardMembership> findByUserId(String userId);

    List<BoardMembership> findByBoardId(String boardId);

    Optional<BoardMembership> findByUserIdAndBoardId(String userId, String boardId);

    boolean existsByUserIdAndBoardId(String userId, String boardId);
}
