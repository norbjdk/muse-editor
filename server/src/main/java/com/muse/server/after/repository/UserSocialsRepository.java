package com.muse.server.after.repository;

import com.muse.server.after.entity.UserSocialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSocialsRepository extends JpaRepository<UserSocialsEntity, Long> {
    Optional<UserSocialsEntity> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    boolean existsByYoutubeId(String youtubeId);
    boolean existsBySpotifyId(String spotifyId);
    boolean existsByInstagramId(String instagramId);
    boolean existsByTiktokId(String tiktokId);

    Optional<UserSocialsEntity> findByYoutubeId(String youtubeId);
    Optional<UserSocialsEntity> findBySpotifyId(String spotifyId);
    Optional<UserSocialsEntity> findByInstagramId(String instagramId);
    Optional<UserSocialsEntity> findByTiktokId(String tiktokId);
}
