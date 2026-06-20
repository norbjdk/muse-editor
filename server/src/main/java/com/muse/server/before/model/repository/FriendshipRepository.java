package com.muse.server.before.model.repository;

import com.muse.server.before.model.entity.FriendshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Long> {
    Optional<FriendshipEntity> findByRequesterIdAndAddresseeId(Long requesterId,Long addresseeId);
    boolean existsByRequesterIdAndAddresseeIdOrRequesterIdAndAddresseeId(
            Long requester1,
            Long addressee1,
            Long requester2,
            Long addressee2
    );
    List<FriendshipEntity>     findAllByRequesterIdOrAddresseeId(Long requesterId, Long addresseeId);
    List<FriendshipEntity>     findAllByAddresseeIdAndStatus(Long addresseeId, FriendshipEntity.FriendshipStatus status);
}
