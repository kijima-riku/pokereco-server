package com.pokereco.pokereco.controller;

import com.pokereco.pokereco.Security.CustomUserPrincipal;
import com.pokereco.pokereco.dto.DeckStatsResponseDto;
import com.pokereco.pokereco.dto.OverAllStatsResponseDto;
import com.pokereco.pokereco.dto.ResultPostRequestDto;
import com.pokereco.pokereco.dto.ResultPostResponseDto;
import com.pokereco.pokereco.dto.OverAllStatsRequestDto;
import com.pokereco.pokereco.responseBuilder.ResponseBuilder;
import com.pokereco.pokereco.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/results")
public class StatsController {
  private final StatsService resultService;
  private final ResponseBuilder responseBuilder;

  StatsController(final StatsService resultService, final ResponseBuilder responseBuilder) {
    this.resultService = resultService;
    this.responseBuilder = responseBuilder;
  }

  @GetMapping("/decks")
  public ResponseEntity<?> getDeckStats(@AuthenticationPrincipal CustomUserPrincipal principal) {
    try {
      Long userId = principal.getUserId();
      List<DeckStatsResponseDto> deckStats = resultService.getDeckStats(userId);
      return responseBuilder.buildSuccessResponse(deckStats);
    } catch (Exception e) {
      return responseBuilder.buildErrorResponse(
          "Failed to get deck stats: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/overall")
  public ResponseEntity<?> getOverallStats(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @ModelAttribute OverAllStatsRequestDto request) {
    try {
      Long userId = principal.getUserId();
      OverAllStatsResponseDto overallStats = resultService.getOverallStats(userId, request);
      return responseBuilder.buildSuccessResponse(overallStats);
    } catch (Exception e) {
      return responseBuilder.buildErrorResponse(
          "Failed to get overall stats: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping
  public ResponseEntity<?> postResult(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @RequestBody ResultPostRequestDto request) {
    try {
      Long userId = principal.getUserId();
      ResultPostResponseDto result = resultService.postResult(userId, request);
      return responseBuilder.buildSuccessResponse(result);
    } catch (Exception e) {
      return responseBuilder.buildErrorResponse(
          "Failed to post result: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
