package com.pokereco.pokereco.service;

import com.pokereco.pokereco.dto.DeckDto;
import com.pokereco.pokereco.dto.DeckRequestDto;
import com.pokereco.pokereco.model.Deck;
import com.pokereco.pokereco.repository.DeckRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeckService {
    private DeckRepository decksRepository;

    public DeckService (final DeckRepository decksRepository) {
        this.decksRepository = decksRepository;
    }

    public List<DeckDto> getAllDecks() {
        List<Deck>  decks = decksRepository.findAll();
        return decks.stream().map(deck -> new DeckDto(deck.getId(), deck.getMainName(), deck.getSubName())).collect(Collectors.toList());
    }

    public DeckDto createDeck(final DeckRequestDto request) {
        Deck newDeck = new Deck(request.getMainName(), request.getSubName());
        Deck deck = decksRepository.save(newDeck);
        return new DeckDto(deck.getId(), deck.getMainName(), deck.getSubName());
    }
}