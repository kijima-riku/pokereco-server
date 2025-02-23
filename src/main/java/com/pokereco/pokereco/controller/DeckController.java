package com.pokereco.pokereco.controller;

import com.pokereco.pokereco.dto.DeckDto;
import com.pokereco.pokereco.dto.DeckRequestDto;
import com.pokereco.pokereco.service.DeckService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deck")
public class DeckController {
    private final DeckService deckService;

    public DeckController(final DeckService deckService){
        this.deckService = deckService;
    }

    @GetMapping
    public List<DeckDto> getDeckList() {
        return  deckService.getAllDecks();
    }

    @PostMapping
    public DeckDto createDeck(@RequestBody DeckRequestDto request) {
        return deckService.createDeck(request);
    }
}