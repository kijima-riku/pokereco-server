package com.pokereco.pokereco.service;

import com.pokereco.pokereco.dto.DeckDto;
import com.pokereco.pokereco.model.Deck;
import com.pokereco.pokereco.model.FavoriteDeck;
import com.pokereco.pokereco.model.UserDeck;
import com.pokereco.pokereco.repository.DeckRepository;
import com.pokereco.pokereco.repository.FavoriteDeckRepository;
import com.pokereco.pokereco.repository.UserDeckRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

@Service
public class UserDeckService {
    private final UserDeckRepository userDeckRepository;
    private final DeckRepository deckRepository;
    private final FavoriteDeckRepository favoriteDeckRepository;

    public UserDeckService(UserDeckRepository userDeckRepository, DeckRepository deckRepository, FavoriteDeckRepository favoriteDeckRepository) {
        this.userDeckRepository = userDeckRepository;
        this.deckRepository = deckRepository;
        this.favoriteDeckRepository = favoriteDeckRepository;
    }

    public List<DeckDto> getUserDecks(Long userId) {
        List<UserDeck> userDecks = userDeckRepository.findByUserId(userId);
        List<Integer> deckIds = userDecks.stream().map(UserDeck::getDeckId).toList();
        List<Deck> decks = deckRepository.findAllById(deckIds);
        return decks.stream().map(deck -> new DeckDto(deck.getId(), deck.getMainName(),deck.getSubName())).toList();
    }

    public UserDeck addUserDeck(Long userId, Integer deckId) {
        UserDeck userDeck = new UserDeck(userId, deckId);
        return userDeckRepository.save(userDeck);
    }

    public Optional<FavoriteDeck> getFavoriteDeck(Long userId){
        Optional<FavoriteDeck> favoriteDeck = favoriteDeckRepository.findByUserId(userId);
        return favoriteDeck.isPresent() ? favoriteDeck : null;
    }

    public FavoriteDeck setFavoriteDeck(Long userId, Integer deckId) {
        Optional<FavoriteDeck>  favoriteDeck = favoriteDeckRepository.findByUserId(userId);
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
        userDeck.ifPresent(userDeckRepository::delete);
    }
}
