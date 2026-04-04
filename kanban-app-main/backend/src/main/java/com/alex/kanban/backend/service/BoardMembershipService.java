package com.alex.kanban.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.alex.kanban.backend.repository.BoardRepository;
import com.alex.kanban.backend.security.SecurityUtils;

import jakarta.transaction.Transactional;
import com.alex.kanban.backend.dto.MembershipRequest;
import com.alex.kanban.backend.dto.MembershipResponse;
import com.alex.kanban.backend.entity.Board;
import com.alex.kanban.backend.entity.BoardMembership;
import com.alex.kanban.backend.entity.BoardRole;
import com.alex.kanban.backend.repository.BoardMembershipRepository;
import com.alex.kanban.backend.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardMembershipService {
    private final BoardMembershipRepository membershipRepository;
    private final BoardRepository boardRepository;

    //Get all members of a board
    public List<MembershipResponse> getMembers(Long boardId) {
        verifyBoardOwner(boardId);
        return membershipRepository.findByBoardId(boardId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    //Add member to board
    @Transactional
    public MembershipResponse addMember(Long boardId, MembershipRequest request) {
        verifyBoardOwner(boardId);
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));

        //Check if already a member
        if (membershipRepository.existsByBoardIdAndAuth0UserId(boardId, request.getAuth0UserId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already a member of this board");
        }

        //Can't add as OWNER - there can only be one owner
        if (request.getRole() == BoardRole.OWNER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot assign owner role to members");
        }

        BoardMembership membership = BoardMembership.builder()
            .board(board)
            .auth0UserId(request.getAuth0UserId())
            .role(request.getRole())
            .build();
        
        return toResponse(membershipRepository.save(membership));
    }

    //Update member role
    @Transactional
    public MembershipResponse updateMemberRole(Long boardId, Long memberId, MembershipRequest request) {
        verifyBoardOwner(boardId);
        BoardMembership membership = membershipRepository.findById(memberId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membership not found"));

        //Can't change owner's role
        if (membership.getRole() == BoardRole.OWNER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot change the owner's role");
        }

        //Can't assign owner role
        if (request.getRole() == BoardRole.OWNER) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot assign owner's role");
        }
        membership.setRole(request.getRole());
        return toResponse(membershipRepository.save(membership));
    }

    //Remove member from board
    @Transactional
    public void removeMember(Long boardId, Long memberId) {
        verifyBoardOwner(boardId);

        BoardMembership membership = membershipRepository.findById(memberId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membership not found"));

        //Can't remove the owner
        if (membership.getRole() == BoardRole.OWNER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cANNOT REMOVE THE BOARD OWNER");

        }
        membershipRepository.delete(membership);
    }
    // Leave  Board (CURRENT USER REMOVES THEMSELVES)
    @Transactional
    public void leaveBoard(Long boardId) {
        String userId = SecurityUtils.getCurrentUserId();
        BoardMembership membership = membershipRepository.findByBoardIdAndAuth0UserId(boardId, userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "You are not a member of this board"));

        if (membership.getRole() == BoardRole.OWNER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Owner cannot leave the board - transfer ownership first");
        
        }
        membershipRepository.delete(membership);
    }

    private Board verifyBoardOwner(Long boardId) {
        String userId = SecurityUtils.getCurrentUserId();

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));

        if (!board.getOwnerAuth0Id().equals(userId)) { throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only board owners can manage members");}
        return board;
    }
    private MembershipResponse toResponse(BoardMembership membership) {
        return MembershipResponse.builder()
            .id(membership.getId())
            .auth0UserId(membership.getAuth0UserId())
            .role(membership.getRole())
            .boardId(membership.getBoard().getId())
            .joinedAt(membership.getJoinedAt())
            .build();
    }
}
