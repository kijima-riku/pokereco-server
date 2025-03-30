package com.pokereco.pokereco.service;

import com.pokereco.pokereco.dto.request.DeckStatsRequestDto;
import com.pokereco.pokereco.dto.request.OverAllStatsRequestDto;
import com.pokereco.pokereco.dto.response.DeckStatsResponseDto;
import com.pokereco.pokereco.dto.response.OverAllStatsResponseDto;
import com.pokereco.pokereco.model.QResult;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StatsService {
  private final JPAQueryFactory jpaQueryFactory;
  private final short OUTCOME_WIN = 1;

  StatsService(final JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
  }

  public List<DeckStatsResponseDto> getDeckStats(
      final Long userId, final DeckStatsRequestDto request) {
    final QResult qr = QResult.result;
    final BooleanBuilder predicate = new BooleanBuilder();
    predicate.and(qr.user.id.eq(userId));

    if (request.isFirst() != null) {
      predicate.and(qr.isFirst.eq(request.isFirst()));
    }

    if (request.startDate() != null) {
      predicate.and(qr.createdAt.goe(request.startDate()));
    }

    if (request.endDate() != null) {
      predicate.and(qr.createdAt.loe(request.endDate()));
    }

    final List<Tuple> results =
        jpaQueryFactory
            .select(
                qr.myDeck.id,
                qr.id.count(),
                qr.outcome.when(OUTCOME_WIN).then(1L).otherwise(0L).sum())
            .from(qr)
            .where(predicate)
            .groupBy(qr.myDeck.id)
            .fetch();

    final List<DeckStatsResponseDto> response = new ArrayList<>();
    for (Tuple record : results) {
      final Integer deckId = record.get(qr.myDeck.id);
      Long totalMatches = record.get(qr.id.count());
      Long totalWins = record.get(qr.outcome.when((short) 1).then(1L).otherwise(0L).sum());
      totalMatches = (totalMatches != null) ? totalMatches : 0L;
      totalWins = (totalWins != null) ? totalWins : 0L;
      double winRate = (totalMatches > 0 ? ((double) totalWins / totalMatches) * 100 : 0.0);
      response.add(new DeckStatsResponseDto(deckId, totalMatches, winRate));
    }
    return response;
  }

  public OverAllStatsResponseDto getOverallStats(
      final Long userId, final OverAllStatsRequestDto request) {
    final QResult qr = QResult.result;
    final BooleanBuilder predicate = new BooleanBuilder();
    predicate.and(qr.user.id.eq(userId));

    if (request.isFirst() != null) {
      predicate.and(qr.isFirst.eq(request.isFirst()));
    }
    if (request.startDate() != null) {
      predicate.and(qr.createdAt.goe(request.startDate()));
    }
    if (request.endDate() != null) {
      predicate.and(qr.createdAt.loe(request.endDate()));
    }

    JPAQuery<Tuple> query =
        jpaQueryFactory
            .select(
                qr.myDeck.id,
                qr.id.count(),
                qr.outcome.when(OUTCOME_WIN).then(1L).otherwise(0L).sum())
            .from(qr)
            .where(predicate)
            .groupBy(qr.myDeck.id);

    if (request.limit() != null) {
      query.limit(request.limit());
      if (request.page() != null) {
        query.offset((long) (request.page() - 1) * request.limit());
      }
    }
    List<Tuple> deckStats = query.fetch();

    long overallMatches = 0;
    long overallWins = 0;
    Integer bestDeckId = null;
    double bestDeckWinRate = 0.0;
    long bestDeckMatches = 0;

    for (Tuple record : deckStats) {
      final Integer deckId = record.get(qr.myDeck.id);
      final Long matches = record.get(qr.id.count());
      final Long wins = record.get(qr.outcome.when((short) 1).then(1L).otherwise(0L).sum());

      overallMatches += matches;
      overallWins += wins;

      double deckWinRate = matches > 0 ? ((double) wins / matches) : 0.0;
      if (deckWinRate > bestDeckWinRate) {
        bestDeckWinRate = deckWinRate;
        bestDeckId = deckId;
        bestDeckMatches = matches;
      }
    }

    double overallWinRate = overallMatches > 0 ? ((double) overallWins / overallMatches) : 0.0;

    return new OverAllStatsResponseDto(
        overallMatches, overallWinRate, bestDeckId, bestDeckWinRate, bestDeckMatches);
  }
}
