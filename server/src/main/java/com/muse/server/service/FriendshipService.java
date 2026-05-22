package com.muse.server.service;

import com.muse.server.model.dto.friend.FriendResponse;
import com.muse.server.model.entity.FriendshipEntity;
import com.muse.server.model.entity.UserEntity;
import com.muse.server.model.repository.FriendshipRepository;
import com.muse.server.model.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    public FriendResponse addFriend(Long userID, Long friendId) {
        final UserEntity requester = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("Requester not found"));

        final UserEntity addressee = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Addressee not found"));

        if (userID.equals(friendId)) {
            throw new RuntimeException("You cannot add yourself");
        }

        boolean exists = friendshipRepository
                .existsByRequesterIdAndAddresseeIdOrRequesterIdAndAddresseeId(
                        userID,
                        friendId,
                        friendId,
                        userID
                );

        if (exists) {
            throw new RuntimeException("Friend request already exists");
        }

        final FriendshipEntity friendshipEntity = new FriendshipEntity();

        friendshipEntity.setRequester(requester);
        friendshipEntity.setAddressee(addressee);
        friendshipEntity.setStatus(FriendshipEntity.FriendshipStatus.PENDING);

        friendshipRepository.save(friendshipEntity);

        final FriendResponse friendResponse = new FriendResponse();

        friendResponse.setId(addressee.getId());
        friendResponse.setUsername(addressee.getUsername());
        friendResponse.setCoverUrl("");

        return friendResponse;
    }

    public FriendResponse acceptFriend(Long addresseeId, Long requesterId) {
        final FriendshipEntity friendship = friendshipRepository
                .findByRequesterIdAndAddresseeId(requesterId, addresseeId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        if (friendship.getStatus() != FriendshipEntity.FriendshipStatus.PENDING) {
            throw new RuntimeException("Friend request already handled");
        }

        friendship.setStatus(FriendshipEntity.FriendshipStatus.ACCEPTED);

        friendshipRepository.save(friendship);

        final UserEntity requester = friendship.getRequester();

        final FriendResponse response = new FriendResponse();

        response.setId(requester.getId());
        response.setUsername(requester.getUsername());
        response.setCoverUrl("");

        return response;
    }

    public FriendResponse rejectFriend(Long addresseeId, Long requesterId) {
        final FriendshipEntity friendship = friendshipRepository
                .findByRequesterIdAndAddresseeId(requesterId, addresseeId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        if (friendship.getStatus() != FriendshipEntity.FriendshipStatus.PENDING) {
            throw new RuntimeException("Friend request already handled");
        }

        friendship.setStatus(FriendshipEntity.FriendshipStatus.REJECTED);

        friendshipRepository.save(friendship);

        final UserEntity requester = friendship.getRequester();

        final FriendResponse response = new FriendResponse();

        response.setId(requesterId);
        response.setUsername(requester.getUsername());
        response.setCoverUrl("");

        return response;
    }

    public List<FriendResponse> getPendingRequests(Long userId) {

        return friendshipRepository
                .findAllByAddresseeIdAndStatus(
                        userId,
                        FriendshipEntity.FriendshipStatus.PENDING
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private FriendResponse toResponse(FriendshipEntity friendship) {

        final UserEntity requester = friendship.getRequester();

        final FriendResponse response = new FriendResponse();

        response.setId(requester.getId());
        response.setUsername(requester.getUsername());
        response.setCoverUrl("");

        return response;
    }
}
