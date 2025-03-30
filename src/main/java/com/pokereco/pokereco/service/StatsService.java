package com.pokereco.pokereco.service;

import com.pokereco.pokereco.dto.DeckStatsResponseDto;
import com.pokereco.pokereco.dto.OverAllStatsResponseDto;
import com.pokereco.pokereco.dto.ResultPostRequestDto;
import com.pokereco.pokereco.dto.ResultPostResponseDto;
import com.pokereco.pokereco.dto.OverAllStatsRequestDto;
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
public class StatsService {
  private final JPAQueryFactory jpaQueryFactory;
  private final ResultRepository resultRepository;
  private final DeckRepository deckRepository;
  private final UserRepository userRepository;
  private final short OUTCOME_WIN = 1;

  StatsService(
      final JPAQueryFactory jpaQueryFactory,
      final ResultRepository resultRepository,
      final DeckRepository deckRepository,
      final UserRepository userRepository) {
    this.jpaQueryFactory = jpaQueryFactory;
    this.resultRepository = resultRepository;
    this.deckRepository = deckRepository;
    this.userRepository = userRepository;
  }

  public List<DeckStatsResponseDto> getDeckStats(Long userId) {
    return getDeckStatsQuery(userId, false);
  }

  public OverAllStatsResponseDto getOverallStats(Long userId, OverAllStatsRequestDto request) {
    final QResult qr = QResult.result;
    final BooleanBuilder predicate = new BooleanBuilder();
    predicate.and(qr.user.id.eq(userId));

    if (request.deckId() != null) {
      Optional<Deck> myDeck = deckRepository.findById(request.deckId());
      myDeck.ifPresent(deck -> predicate.and(qr.myDeck.eq(deck)));
    }
    if (request.opponentDeckId() != null) {
      Optional<Deck> opponentDeck = deckRepository.findById(request.opponentDeckId());
      opponentDeck.ifPresent(deck -> predicate.and(qr.opponentDeck.eq(deck)));
    }
    if (request.outcome() != null) {
      predicate.and(qr.outcome.eq(request.outcome()));
    }
    if (request.isFirst() != null) {
      predicate.and(qr.isFirst.eq(request.isFirst()));
    }
    if (request.startDate() != null) {
      predicate.and(qr.createdAt.goe(LocalDate.parse(request.startDate()).atStartOfDay()));
    }
    if (request.endDate() != null) {
      predicate.and(qr.createdAt.loe(LocalDate.parse(request.endDate()).atTime(23, 59, 59)));
    }
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
    totalMatches = (totalMatches != null) ? totalMatches : 0L;
    totalWins = (totalWins != null) ? totalWins : 0L;
    double winRate = (totalMatches > 0 ? (double) totalWins / totalMatches : 0.0);
    OverAllStatsResponseDto bestDeckStats = getBestWinRateDeck(userId);
    return new OverAllStatsResponseDto(
        totalMatches, winRate * 100, bestDeckStats.bestDeckId(), bestDeckStats.winRate());
  }

  public ResultPostResponseDto postResult(Long userId, ResultPostRequestDto request) {
    User user = userRepository.getReferenceById(userId);
    Deck myDeck = deckRepository.getReferenceById(request.myDeck());
    Deck opponentDeck = deckRepository.getReferenceById(request.opponentDeck());

    Result result =
        new Result(
            user, myDeck, opponentDeck, request.isFirst(), request.turnCount(), request.outcome());
    Result savedResult = resultRepository.save(result);
    return new ResultPostResponseDto(
        savedResult.getId(),
        savedResult.getMyDeck().getId(),
        savedResult.getOpponentDeck().getId(),
        savedResult.isFirst(),
        savedResult.getTurnCount(),
        savedResult.getOutcome());
  }

  private List<OverAllStatsResponseDto> getDeckStatsQuery(Long userId, boolean singleBestDeck) {
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

    List<DeckStatsResponseDto> response = new ArrayList<>();
    for (Tuple record : deckStats) {
      Integer deckId = record.get(qr.myDeck.id);
      Long totalMatches = record.get(qr.id.count());
      Long totalWins = record.get(qr.outcome.when((short) 1).then(1L).otherwise(0L).sum());
      totalMatches = (totalMatches != null) ? totalMatches : 0L;
      totalWins = (totalWins != null) ? totalWins : 0L;
      double winRate = (totalMatches > 0 ? (double) totalWins / totalMatches * 100 : 0.0);
      response.add(new DeckStatsResponseDto(deckId, totalMatches, winRate));

      if (singleBestDeck) break;
    }
    return response;
  }

  private OverAllStatsResponseDto getBestWinRateDeck(Long userId) {
    List<OverAllStatsResponseDto> bestDeckStats = getDeckStatsQuery(userId, true);
    if (bestDeckStats.isEmpty()) {
      return new OverAllStatsResponseDto(0L, 0.0, null, 0.0);
    } else {
      OverAllStatsResponseDto dto = bestDeckStats.get(0);
      return new OverAllStatsResponseDto(
          dto.totalMatches(), dto.winRate(), dto.bestDeckId(), dto.winRate());
    }
  }
}
