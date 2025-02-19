package com.pokereco.pokereco.repository;

import com.pokereco.pokereco.model.Decks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecksRepository extends JpaRepository<Decks, Integer> {
}
