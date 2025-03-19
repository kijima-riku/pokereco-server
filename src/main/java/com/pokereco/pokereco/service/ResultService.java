package com.pokereco.pokereco.service;

import com.pokereco.pokereco.dto.MatchDto;
import com.pokereco.pokereco.dto.ResultDeckStatsDto;
import com.pokereco.pokereco.dto.ResultPostRequestDto;
import com.pokereco.pokereco.dto.ResultPostResponseDto;
import com.pokereco.pokereco.dto.ResultRequestDto;
import com.pokereco.pokereco.model.Deck;
import com.pokereco.pokereco.model.QResult;
import com.pokereco.pokereco.model.Result;
import com.pokereco.pokereco.model.User;
import com.pokereco.pokereco.repository.DeckRepository;
import com.pokereco.pokereco.repository.ResultRepository;
import com.pokereco.pokereco.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ResultService {
  private final JPAQueryFactory jpaQueryFactory;
  private final ResultRepository resultRepository;
  private final DeckRepository deckRepository;
  private final UserRepository userRepository;
  private final int DEFAULT_LIMIT = 15;
  private final short OUTCOME_WIN = 1;

  public ResultService(
      final JPAQueryFactory jpaQueryFactory,
      final ResultRepository resultRepository,
      final DeckRepository deckRepository,
      final UserRepository userRepository) {
    this.jpaQueryFactory = jpaQueryFactory;
    this.resultRepository = resultRepository;
    this.deckRepository = deckRepository;
    this.userRepository = userRepository;
  }

  public List<MatchDto> getResults(Long userId, ResultRequestDto request) {
    final QResult qr = QResult.result;
    final BooleanBuilder predicate = new BooleanBuilder();
    predicate.and(qr.user.id.eq(userId));

    if (request.getDeckId() != null) {
      Optional<Deck> myDeck = deckRepository.findById(request.getDeckId());
      myDeck.ifPresent(deck -> predicate.and(qr.myDeck.eq(deck)));
    }
    if (request.getOpponentDeckId() != null) {
      Optional<Deck> opponentDeck = deckRepository.findById(request.getOpponentDeckId());
      opponentDeck.ifPresent(deck -> predicate.and(qr.opponentDeck.eq(deck)));
    }
    if (request.getOutcome() != null) {
      predicate.and(qr.outcome.eq(request.getOutcome()));
    }
    if (request.getIsFirst() != null) {
      predicate.and(qr.isFirst.eq(request.getIsFirst()));
    }
    if (request.getStartDate() != null) {
      predicate.and(qr.createdAt.goe(LocalDate.parse(request.getStartDate()).atStartOfDay()));
    }
    if (request.getEndDate() != null) {
      predicate.and(qr.createdAt.loe(LocalDate.parse(request.getEndDate()).atTime(23, 59, 59)));
    }

    final List<Result> resultModels =
        jpaQueryFactory
            .selectFrom(qr)
            .where(predicate)
            .orderBy(qr.createdAt.desc())
            .limit(Optional.ofNullable(request.getLimit()).orElse(DEFAULT_LIMIT))
            .offset(
                Optional.ofNullable(request.getPage())
                    .map(p -> (p - 1) * request.getLimit())
                    .orElse(0))
            .fetch();
    return resultModels.stream()
        .map(
            r ->
                new MatchDto(
                    r.getId(),
                    r.getUser().getId(),
                    r.getMyDeck(),
                    r.getOpponentDeck(),
                    r.isFirst(),
                    r.getTurnCount(),
                    r.getOutcome(),
                    r.getCreatedAt()))
        .toList();
  }

  public List<ResultDeckStatsDto> getDeckStats(Long userId) {
    return getDeckStatsQuery(userId, false);
  }

  public ResultDeckStatsDto getOverallStats(Long userId, ResultRequestDto request) {
    final QResult qr = QResult.result;
    final BooleanBuilder predicate = new BooleanBuilder();
    predicate.and(qr.user.id.eq(userId));

    if (request.getDeckId() != null) {
      Optional<Deck> myDeck = deckRepository.findById(request.getDeckId());
      myDeck.ifPresent(deck -> predicate.and(qr.myDeck.eq(deck)));
    }
    if (request.getOpponentDeckId() != null) {
      Optional<Deck> opponentDeck = deckRepository.findById(request.getOpponentDeckId());
      opponentDeck.ifPresent(deck -> predicate.and(qr.opponentDeck.eq(deck)));
    }
    if (request.getOutcome() != null) {
      predicate.and(qr.outcome.eq(request.getOutcome()));
    }
    if (request.getIsFirst() != null) {
      predicate.and(qr.isFirst.eq(request.getIsFirst()));
    }
    if (request.getStartDate() != null) {
      predicate.and(qr.createdAt.goe(LocalDate.parse(request.getStartDate()).atStartOfDay()));
    }
    if (request.getEndDate() != null) {
      predicate.and(qr.createdAt.loe(LocalDate.parse(request.getEndDate()).atTime(23, 59, 59)));
    }
    System.out.println(predicate + "predicate");
    Tuple record =
        jpaQueryFactory
            .select(qr.id.count(), qr.outcome.when(OUTCOME_WIN).then(1L).otherwise(0L).sum())
            .from(qr)
            .where(qr.user.id.eq(userId))
            .where(predicate)
            .fetchOne();
    Long totalMatches = record != null ? record.get(qr.id.count()) : 0L;
    Long totalWins =
        record != null ? record.get(qr.outcome.when((short) 1).then(1L).otherwise(0L).sum()) : 0L;
    System.out.println(totalWins + " totalWins");
    totalMatches = (totalMatches != null) ? totalMatches : 0L;
    totalWins = (totalWins != null) ? totalWins : 0L;
    double winRate = (totalMatches > 0 ? (double) totalWins / totalMatches : 0.0);
    ResultDeckStatsDto bestDeckStats = getBestWinRateDeck(userId);
    return new ResultDeckStatsDto(
        totalMatches, winRate * 100, bestDeckStats.getBestDeckId(), bestDeckStats.getWinRate());
  }

  public ResultPostResponseDto postResult(Long userId, ResultPostRequestDto request) {
    User user = userRepository.getReferenceById(userId);
    Deck myDeck = deckRepository.getReferenceById(request.getMyDeck());
    Deck opponentDeck = deckRepository.getReferenceById(request.getOpponentDeck());

    Result result =
        new Result(
            user,
            myDeck,
            opponentDeck,
            request.isIsFirst(),
            request.getTurnCount(),
            request.getOutcome());
    Result savedResult = resultRepository.save(result);
    return new ResultPostResponseDto(
        savedResult.getId(),
        savedResult.getMyDeck().getId(),
        savedResult.getOpponentDeck().getId(),
        savedResult.isFirst(),
        savedResult.getTurnCount(),
        savedResult.getOutcome());
  }

  private List<ResultDeckStatsDto> getDeckStatsQuery(Long userId, boolean singleBestDeck) {
    QResult qr = QResult.result;
    JPAQuery<Tuple> query =
        jpaQueryFactory
            .select(
                qr.myDeck.id,
                qr.id.count(),
                qr.outcome.when(OUTCOME_WIN).then(1L).otherwise(0L).sum())
            .from(qr)
            .where(qr.user.id.eq(userId))
            .groupBy(qr.myDeck.id);

    if (singleBestDeck) {
      query.orderBy(
          qr.outcome.when(OUTCOME_WIN).then(1L).otherwise(0L).sum().divide(qr.id.count()).desc());
    }
    List<Tuple> deckStats = query.fetch();

    List<ResultDeckStatsDto> response = new ArrayList<>();
    for (Tuple record : deckStats) {
      Integer deckId = record.get(qr.myDeck.id);
      Long totalMatches = record.get(qr.id.count());
      Long totalWins = record.get(qr.outcome.when((short) 1).then(1L).otherwise(0L).sum());
      totalMatches = (totalMatches != null) ? totalMatches : 0L;
      totalWins = (totalWins != null) ? totalWins : 0L;
      double winRate = (totalMatches > 0 ? (double) totalWins / totalMatches * 100 : 0.0);
      response.add(new ResultDeckStatsDto(deckId, totalMatches, winRate));

      if (singleBestDeck) break;
    }
    return response;
  }

  private ResultDeckStatsDto getBestWinRateDeck(Long userId) {
    List<ResultDeckStatsDto> bestDeckStats = getDeckStatsQuery(userId, true);
    if (bestDeckStats.isEmpty()) {
      return new ResultDeckStatsDto(0L, 0.0, null, 0.0);
    } else {
      ResultDeckStatsDto dto = bestDeckStats.get(0);
      return new ResultDeckStatsDto(
          dto.getTotalMatches(), dto.getWinRate(), dto.getDeckId(), dto.getWinRate());
    }
  }
}
