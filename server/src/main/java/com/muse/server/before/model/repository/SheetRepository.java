package com.muse.server.before.model.repository;

import com.muse.server.before.model.entity.SheetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SheetRepository extends JpaRepository<SheetEntity, Long> {
    List<SheetEntity> findByProjectId(Long projectId);
}
