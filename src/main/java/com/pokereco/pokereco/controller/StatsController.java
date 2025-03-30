package com.pokereco.pokereco.controller;

import com.pokereco.pokereco.Security.CustomUserPrincipal;
import com.pokereco.pokereco.dto.request.DeckStatsRequestDto;
import com.pokereco.pokereco.dto.request.OverAllStatsRequestDto;
import com.pokereco.pokereco.dto.response.DeckStatsResponseDto;
import com.pokereco.pokereco.dto.response.OverAllStatsResponseDto;
import com.pokereco.pokereco.responseBuilder.ResponseBuilder;
import com.pokereco.pokereco.service.StatsService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stats")
public class StatsController {
  private final StatsService resultService;
  private final ResponseBuilder responseBuilder;

  StatsController(final StatsService resultService, final ResponseBuilder responseBuilder) {
    this.resultService = resultService;
    this.responseBuilder = responseBuilder;
  }

  @GetMapping("/decks")
  public ResponseEntity<?> getDeckStats(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @ModelAttribute DeckStatsRequestDto request) {
    try {
      final Long userId = principal.userId();
      final List<DeckStatsResponseDto> deckStats = resultService.getDeckStats(userId, request);
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
      final Long userId = principal.userId();
      final OverAllStatsResponseDto overallStats = resultService.getOverallStats(userId, request);
      return responseBuilder.buildSuccessResponse(overallStats);
    } catch (Exception e) {
      return responseBuilder.buildErrorResponse(
          "Failed to get overall stats: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
