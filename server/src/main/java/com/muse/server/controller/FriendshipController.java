package com.muse.server.controller;

import com.muse.server.model.detail.CustomUserDetails;
import com.muse.server.model.dto.friend.FriendResponse;
import com.muse.server.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friendships")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @PostMapping("/requests/{userId}")
    public ResponseEntity<FriendResponse> add(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                friendshipService.addFriend(user.getId(), userId)
        );
    }

    @PostMapping("/requests/{userId}/accept")
    public ResponseEntity<FriendResponse> accept(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                friendshipService.acceptFriend(user.getId(), userId)
        );
    }

    @PostMapping("/requests/{userId}/reject")
    public ResponseEntity<FriendResponse> reject(
            @AuthenticationPrincipal CustomUserDetails  user,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                friendshipService.rejectFriend(user.getId(), userId)
        );
    }

    @GetMapping("/requests")
    public ResponseEntity<List<FriendResponse>> getRequests(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(
                friendshipService.getPendingRequests(user.getId())
        );
    }
}
