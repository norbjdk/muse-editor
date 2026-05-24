package com.muse.server.model.repository;

import com.muse.server.model.entity.SheetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SheetRepository extends JpaRepository<SheetEntity, Long> {
    List<SheetEntity> findByProjectId(Long projectId);
}
