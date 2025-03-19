package com.pokereco.pokereco.controller;

import com.pokereco.pokereco.Security.CustomUserPrincipal;
import com.pokereco.pokereco.dto.MatchDto;
import com.pokereco.pokereco.dto.ResultDeckStatsDto;
import com.pokereco.pokereco.dto.ResultPostRequestDto;
import com.pokereco.pokereco.dto.ResultPostResponseDto;
import com.pokereco.pokereco.dto.ResultRequestDto;
import com.pokereco.pokereco.responseBuilder.ResponseBuilder;
import com.pokereco.pokereco.service.ResultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/results")
public class ResultController {
  private final ResultService resultService;
  private final ResponseBuilder responseBuilder;

  public ResultController(final ResultService resultService, final ResponseBuilder responseBuilder) {
    this.resultService = resultService;
    this.responseBuilder = responseBuilder;
  }

  @GetMapping
  public ResponseEntity<?> getResults(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @ModelAttribute ResultRequestDto request) {
    try {
      Long userId = principal.getUserId();
      List<MatchDto> results = resultService.getResults(userId, request);
      return responseBuilder.buildSuccessResponse(results);
    } catch (Exception e) {
      return responseBuilder.buildErrorResponse("Failed to get results: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/decks")
  public ResponseEntity<?> getDeckStats(@AuthenticationPrincipal CustomUserPrincipal principal) {
    try {
      Long userId = principal.getUserId();
      List<ResultDeckStatsDto> deckStats = resultService.getDeckStats(userId);
      return responseBuilder.buildSuccessResponse(deckStats);
    } catch (Exception e) {
      return responseBuilder.buildErrorResponse("Failed to get deck stats: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/overall")
  public ResponseEntity<?> getOverallStats(@AuthenticationPrincipal CustomUserPrincipal principal) {
    try {
      Long userId = principal.getUserId();
      ResultDeckStatsDto overallStats = resultService.getOverallStats(userId);
      return responseBuilder.buildSuccessResponse(overallStats);
    } catch (Exception e) {
      return responseBuilder.buildErrorResponse("Failed to get overall stats: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
      return responseBuilder.buildErrorResponse("Failed to post result: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
