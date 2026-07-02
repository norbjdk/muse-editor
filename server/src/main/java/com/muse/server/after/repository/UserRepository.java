package com.muse.server.after.repository;

import com.muse.server.after.entity.UserEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);

    @Override
    <S extends UserEntity> boolean exists(@NonNull Example<S> example);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
