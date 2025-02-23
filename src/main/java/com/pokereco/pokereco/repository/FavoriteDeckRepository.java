package com.pokereco.pokereco.repository;

import com.pokereco.pokereco.model.FavoriteDeck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteDeckRepository extends JpaRepository<FavoriteDeck, Integer> {
    Optional<FavoriteDeck> findByUserId(Long usrId);
}
