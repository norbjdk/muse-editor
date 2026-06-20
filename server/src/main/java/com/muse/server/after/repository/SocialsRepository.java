package com.muse.server.after.repository;

import com.muse.server.before.model.entity.SocialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialsRepository extends JpaRepository<SocialEntity, Long> {
}
