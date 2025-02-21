package com.pokereco.pokereco.service;

import com.pokereco.pokereco.dto.DecksListDto;
import com.pokereco.pokereco.model.Deck;
import com.pokereco.pokereco.repository.DecksRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeckService {
    private DecksRepository decksRepository;

    public DeckService (final DecksRepository decksRepository) {
        this.decksRepository = decksRepository;
    }

    public List<DecksListDto> getAllDecks() {
        List<Deck>  decks = decksRepository.findAll();
        return decks.stream().map(deck -> new DecksListDto(deck.getId(), deck.getName())).collect(Collectors.toList());
    }
}