package com.pokereco.pokereco.repository;

import com.pokereco.pokereco.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserKey(UUID userKey);
}
