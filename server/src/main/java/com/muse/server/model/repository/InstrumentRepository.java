package com.muse.server.model.repository;

import com.muse.server.model.entity.InstrumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstrumentRepository extends JpaRepository<InstrumentEntity, Long> {
    Optional<InstrumentEntity> findByName(String name);
}
