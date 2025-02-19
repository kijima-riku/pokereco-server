package com.pokereco.pokereco.controller;

import com.pokereco.pokereco.dto.DecksListDto;
import com.pokereco.pokereco.service.DeckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deck")
public class DeckController {
    private final DeckService deckService;

    public DeckController(final DeckService deckService){
        this.deckService = deckService;
    }

    @GetMapping("/list")
    public List<DecksListDto> getDeckList() {
        return  deckService.getAllDecks();
    }
}