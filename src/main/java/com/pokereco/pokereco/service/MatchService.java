package com.pokereco.pokereco.service;

import com.pokereco.pokereco.dto.MatchDto;
import com.pokereco.pokereco.dto.OverAllStatsRequestDto;
import com.pokereco.pokereco.model.Deck;
import com.pokereco.pokereco.model.QResult;
import com.pokereco.pokereco.model.Result;
import com.pokereco.pokereco.repository.DeckRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MatchService {
  final JPAQueryFactory jpaQueryFactory;
  private final DeckRepository deckRepository;

  static final int DEFAULT_LIMIT = 15;

  MatchService(final DeckRepository deckRepository, final JPAQueryFactory jpaQueryFactory) {
    this.deckRepository = deckRepository;
    this.jpaQueryFactory = jpaQueryFactory;
  }

  public List<MatchDto> getResults(Long userId, OverAllStatsRequestDto request) {
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

    final List<Result> resultModels =
        jpaQueryFactory
            .selectFrom(qr)
            .where(predicate)
            .orderBy(qr.createdAt.desc())
            .limit(Optional.ofNullable(request.limit()).orElse(DEFAULT_LIMIT))
            .offset(
                Optional.ofNullable(request.page()).map(p -> (p - 1) * request.limit()).orElse(0))
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
}
