package com.pokereco.pokereco.controller;

import com.pokereco.pokereco.dto.DeckDto;
import com.pokereco.pokereco.dto.DeckRequestDto;
import com.pokereco.pokereco.responseBuilder.ResponseBuilder;
import com.pokereco.pokereco.service.DeckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deck")
public class DeckController {
  private final DeckService deckService;
  private final ResponseBuilder responseBuilder;

  DeckController(final DeckService deckService, final ResponseBuilder responseBuilder) {
    this.deckService = deckService;
    this.responseBuilder = responseBuilder;
  }

  @GetMapping
  public ResponseEntity<?> getDeckList() {
    final List<DeckDto> deckList = deckService.getAllDecks();
    return responseBuilder.buildSuccessResponse(deckList);
  }

  @PostMapping
  public ResponseEntity<?> createDeck(@RequestBody DeckRequestDto request) {
    final DeckDto createdDeck = deckService.createDeck(request);
    return responseBuilder.buildSuccessResponse(createdDeck);
  }
}
