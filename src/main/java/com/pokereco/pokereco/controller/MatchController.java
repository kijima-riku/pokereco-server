package com.pokereco.pokereco.controller;

import com.pokereco.pokereco.Security.CustomUserPrincipal;
import com.pokereco.pokereco.dto.MatchDto;
import com.pokereco.pokereco.dto.OverAllStatsRequestDto;
import com.pokereco.pokereco.responseBuilder.ResponseBuilder;
import com.pokereco.pokereco.service.MatchService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/matches")
public class MatchController {
  private final ResponseBuilder responseBuilder;
  private final MatchService matchService;

  MatchController(final ResponseBuilder responseBuilder, final MatchService matchService) {
    this.responseBuilder = responseBuilder;
    this.matchService = matchService;
  }

  @GetMapping
  public ResponseEntity<?> getResults(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @ModelAttribute OverAllStatsRequestDto request) {
    try {
      Long userId = principal.getUserId();
      List<MatchDto> results = matchService.getResults(userId, request);
      return responseBuilder.buildSuccessResponse(results);
    } catch (Exception e) {
      return responseBuilder.buildErrorResponse(
          "Failed to get results: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
