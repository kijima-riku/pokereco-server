package com.pokereco.pokereco.controller;

import com.pokereco.pokereco.Security.CustomUserPrincipal;
import com.pokereco.pokereco.dto.ResultDeckStatsDto;
import com.pokereco.pokereco.dto.ResultRequestDto;
import com.pokereco.pokereco.model.Result;
import com.pokereco.pokereco.service.ResultService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/results")
public class ResultController {
    private final ResultService resultService;

    public ResultController (final ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping
    public List<Result> getResults(@AuthenticationPrincipal CustomUserPrincipal principal, @ModelAttribute ResultRequestDto request){
        Long userId = principal.getUserId();
        return resultService.getResults(userId, request);
    }

    @GetMapping("/decks")
    public List<ResultDeckStatsDto> getDeckStats(@AuthenticationPrincipal CustomUserPrincipal principal) {
        Long userId = principal.getUserId();
        return resultService.getDeckStats(userId);
    }
}
