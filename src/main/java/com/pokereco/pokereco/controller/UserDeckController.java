package com.pokereco.pokereco.controller;

import com.pokereco.pokereco.Security.CustomUserPrincipal;
import com.pokereco.pokereco.dto.DeckDto;
import com.pokereco.pokereco.dto.UserDeckRequestDto;
import com.pokereco.pokereco.model.FavoriteDeck;
import com.pokereco.pokereco.model.UserDeck;
import com.pokereco.pokereco.service.UserDeckService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-decks")
public class UserDeckController {

    private final UserDeckService userDeckService;

    public UserDeckController(UserDeckService userDeckService) {
        this.userDeckService = userDeckService;
    }

    @GetMapping
    public List<DeckDto> getUserDecks(@AuthenticationPrincipal CustomUserPrincipal principal) {
        Long userId = principal.getUserId();
        return userDeckService.getUserDecks(userId);
    }

    @PostMapping
    public UserDeck addUserDeck(@AuthenticationPrincipal CustomUserPrincipal principal , @RequestBody UserDeckRequestDto dto) {
        Long userId = principal.getUserId();
        return userDeckService.addUserDeck(userId, dto.getDeckId());
    }

    @PatchMapping("/favorite")
    public FavoriteDeck setFavoriteDeck(@AuthenticationPrincipal CustomUserPrincipal principal, @RequestBody UserDeckRequestDto dto) {
        Long userId = principal.getUserId();
        System.out.println(userId);
        System.out.println(dto.getDeckId());
        return userDeckService.setFavoriteDeck(userId, dto.getDeckId());
    }

    @DeleteMapping("/{deckId}")
    public void deleteUserDeck(@AuthenticationPrincipal CustomUserPrincipal principal, @PathVariable("deckId") Integer deckId) {
        Long userId = principal.getUserId();
        userDeckService.removeUserDeck(userId, deckId);
    }
}
