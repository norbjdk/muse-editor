package com.muse.server.model.repository;

import com.muse.server.model.entity.FriendshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Long> {
    Optional<FriendshipEntity> findByRequesterIdAndAddresseeId(Long requesterId,Long addresseeId);
    List<FriendshipEntity>     findAllByRequesterIdOrAddresseeId(Long requesterId, Long addresseeId);
    List<FriendshipEntity>     findAllByAddresseeIdAndStatus(Long addresseeId, FriendshipEntity.FriendshipStatus status);
}
