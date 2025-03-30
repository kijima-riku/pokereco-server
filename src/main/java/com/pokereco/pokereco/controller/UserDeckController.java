package com.pokereco.pokereco.controller;

import com.pokereco.pokereco.Security.CustomUserPrincipal;
import com.pokereco.pokereco.dto.DeckDto;
import com.pokereco.pokereco.dto.FavoriteDeckResponseDto;
import com.pokereco.pokereco.dto.UserDeckRequestDto;
import com.pokereco.pokereco.dto.UserDeckResponseDto;
import com.pokereco.pokereco.model.FavoriteDeck;
import com.pokereco.pokereco.responseBuilder.ResponseBuilder;
import com.pokereco.pokereco.service.UserDeckService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-decks")
public class UserDeckController {

  private final UserDeckService userDeckService;
  private final ResponseBuilder responseBuilder;

  UserDeckController(final UserDeckService userDeckService, final ResponseBuilder responseBuilder) {
    this.userDeckService = userDeckService;
    this.responseBuilder = responseBuilder;
  }

  @GetMapping
  public ResponseEntity<?> getUserDecks(@AuthenticationPrincipal CustomUserPrincipal principal) {
    try {
      Long userId = principal.getUserId();
      List<DeckDto> decks = userDeckService.getUserDecks(userId);
      if (!decks.isEmpty()) {
        return responseBuilder.buildSuccessResponse(decks);
      } else {
        return responseBuilder.buildErrorResponse(
            "Registered user deck is not found.", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return responseBuilder.buildErrorResponse(
          "An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping
  public ResponseEntity<?> addUserDeck(
      @AuthenticationPrincipal CustomUserPrincipal principal, @RequestBody UserDeckRequestDto dto) {
    try {
      Long userId = principal.getUserId();
      UserDeckResponseDto newDeck = userDeckService.addUserDeck(userId, dto.deckId());
      return responseBuilder.buildSuccessResponse(newDeck);
    } catch (IllegalArgumentException e) {
      return responseBuilder.buildErrorResponse(e.getMessage(), HttpStatus.CONFLICT);
    } catch (Exception e) {
      return responseBuilder.buildErrorResponse(
          "An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/favorite")
  public ResponseEntity<?> getFavoriteDeck(@AuthenticationPrincipal CustomUserPrincipal principal) {
    try {
      Long userId = principal.getUserId();
      List<FavoriteDeckResponseDto> favoriteDeck = userDeckService.getFavoriteDeck(userId);
      if (!favoriteDeck.isEmpty()) {
        return responseBuilder.buildSuccessResponse(favoriteDeck);
      } else {
        return responseBuilder.buildErrorResponse(
            "favorite deck is not found.", HttpStatus.NOT_FOUND);
      }
    } catch (IllegalArgumentException e) {
      return responseBuilder.buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return responseBuilder.buildErrorResponse(
          "An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PatchMapping("/favorite")
  public ResponseEntity<?> setFavoriteDeck(
      @AuthenticationPrincipal CustomUserPrincipal principal, @RequestBody UserDeckRequestDto dto) {
    try {
      Long userId = principal.getUserId();
      FavoriteDeck favoriteDeck = userDeckService.setFavoriteDeck(userId, dto.deckId());
      return responseBuilder.buildSuccessResponse(favoriteDeck);
    } catch (Exception e) {
      return responseBuilder.buildErrorResponse(
          "An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{deckId}")
  public ResponseEntity<?> deleteUserDeck(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @PathVariable("deckId") Integer deckId) {
    try {
      Long userId = principal.getUserId();
      userDeckService.removeUserDeck(userId, deckId);
      return responseBuilder.buildSuccessResponse("User deck deleted successfully.");
    } catch (IllegalArgumentException e) {
      return responseBuilder.buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return responseBuilder.buildErrorResponse(
          "An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
