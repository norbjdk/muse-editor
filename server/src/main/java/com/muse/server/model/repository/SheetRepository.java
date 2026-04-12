package com.muse.server.model.repository;

import com.muse.server.model.entity.SheetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SheetRepository extends JpaRepository<SheetEntity, Long> {
    List<SheetEntity> findByUserId(Long userId);
    List<SheetEntity> findByUserIdAndTitleContaining(Long userId, String title);

    List<SheetEntity> findByInstrument(String instrument);

    @Query("SELECT t FROM SheetEntity t ORDER BY t.plays DESC")
    List<SheetEntity> findTopSheets();

    @Query("SELECT t FROM SheetEntity t ORDER BY t.createdAt DESC")
    List<SheetEntity> findNewestSheets();
}
