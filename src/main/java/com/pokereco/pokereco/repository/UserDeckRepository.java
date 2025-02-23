package com.pokereco.pokereco.repository;

import com.pokereco.pokereco.model.UserDeck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDeckRepository extends JpaRepository<UserDeck, Integer> {
    List<UserDeck> findByUserId(Long userId);
    Optional<UserDeck> findByUserIdAndDeckId(Long userId, Integer deckId);
}