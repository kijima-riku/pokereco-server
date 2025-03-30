package com.pokereco.pokereco.service;

import com.pokereco.pokereco.dto.DeckDto;
import com.pokereco.pokereco.dto.FavoriteDeckResponseDto;
import com.pokereco.pokereco.dto.UserDeckResponseDto;
import com.pokereco.pokereco.model.Deck;
import com.pokereco.pokereco.model.FavoriteDeck;
import com.pokereco.pokereco.model.UserDeck;
import com.pokereco.pokereco.repository.DeckRepository;
import com.pokereco.pokereco.repository.FavoriteDeckRepository;
import com.pokereco.pokereco.repository.UserDeckRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserDeckService {
  private final UserDeckRepository userDeckRepository;
  private final DeckRepository deckRepository;
  private final FavoriteDeckRepository favoriteDeckRepository;

  UserDeckService(
      UserDeckRepository userDeckRepository,
      DeckRepository deckRepository,
      FavoriteDeckRepository favoriteDeckRepository) {
    this.userDeckRepository = userDeckRepository;
    this.deckRepository = deckRepository;
    this.favoriteDeckRepository = favoriteDeckRepository;
  }

  public List<DeckDto> getUserDecks(Long userId) {
    List<UserDeck> userDecks = userDeckRepository.findByUserId(userId);
    List<Integer> deckIds = userDecks.stream().map(UserDeck::getDeckId).toList();
    List<Deck> decks = deckRepository.findAllById(deckIds);
    return decks.stream()
        .map(deck -> new DeckDto(deck.getId(), deck.getMainName(), deck.getSubName()))
        .toList();
  }

  public UserDeckResponseDto addUserDeck(Long userId, Integer deckId) {
    Optional<UserDeck> existing = userDeckRepository.findByUserIdAndDeckId(userId, deckId);
    if (existing.isPresent()) {
      throw new IllegalArgumentException(
          "The deck (ID: " + deckId + ") is already registered for the user.");
    }
    UserDeck userDeck = new UserDeck(userId, deckId);
    final UserDeck added = userDeckRepository.save(userDeck);
    return new UserDeckResponseDto(added.getDeckId(), added.getCreatedAt());
  }

  public List<FavoriteDeckResponseDto> getFavoriteDeck(Long userId) {
    final List<FavoriteDeck> favoriteDeck = favoriteDeckRepository.getFavoriteDecksByUserId(userId);
    if (favoriteDeck.isEmpty()) {
      throw new IllegalArgumentException("favorite deck is not found.");
    }
    return favoriteDeck.stream().map(id -> new FavoriteDeckResponseDto(id.getDeckId())).toList();
  }

  // ここのロジック
  public FavoriteDeck setFavoriteDeck(Long userId, Integer deckId) {
    Optional<FavoriteDeck> favoriteDeck = favoriteDeckRepository.findByUserId(userId);
    if (favoriteDeck.isPresent()) {
      FavoriteDeck updateFavoriteDeck = favoriteDeck.get();
      updateFavoriteDeck.setDeckId(deckId);
      return favoriteDeckRepository.save(updateFavoriteDeck);
    } else {
      FavoriteDeck newFavoriteDeck = new FavoriteDeck(userId, deckId);
      return favoriteDeckRepository.save(newFavoriteDeck);
    }
  }

  @Transactional
  public void removeUserDeck(Long userId, Integer deckId) {
    Optional<UserDeck> userDeck = userDeckRepository.findByUserIdAndDeckId(userId, deckId);
    if (userDeck.isEmpty()) {
      throw new IllegalArgumentException(
          "The deck (ID: " + deckId + ") is not registered for the user.");
    }
    userDeckRepository.delete(userDeck.get());
  }
}
