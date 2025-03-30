package com.pokereco.pokereco.service;

import com.pokereco.pokereco.dto.MatchDto;
import com.pokereco.pokereco.dto.request.MatchRequestDto;
import com.pokereco.pokereco.dto.request.ResultPostRequestDto;
import com.pokereco.pokereco.dto.response.ResultPostResponseDto;
import com.pokereco.pokereco.model.Deck;
import com.pokereco.pokereco.model.QResult;
import com.pokereco.pokereco.model.Result;
import com.pokereco.pokereco.model.User;
import com.pokereco.pokereco.repository.DeckRepository;
import com.pokereco.pokereco.repository.ResultRepository;
import com.pokereco.pokereco.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MatchService {
  final JPAQueryFactory jpaQueryFactory;
  private final DeckRepository deckRepository;
  private final UserRepository userRepository;
  private final ResultRepository resultRepository;

  static final int DEFAULT_LIMIT = 15;

  MatchService(
      final DeckRepository deckRepository,
      final JPAQueryFactory jpaQueryFactory,
      final UserRepository userRepository,
      final ResultRepository resultRepository) {
    this.deckRepository = deckRepository;
    this.jpaQueryFactory = jpaQueryFactory;
    this.userRepository = userRepository;
    this.resultRepository = resultRepository;
  }

  public List<MatchDto> getResults(Long userId, MatchRequestDto request) {
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
    if (request.isFirst() != null) {
      predicate.and(qr.isFirst.eq(request.isFirst()));
    }
    if (request.startDate() != null) {
      predicate.and(qr.createdAt.goe(request.startDate()));
    }
    if (request.endDate() != null) {
      predicate.and(qr.createdAt.loe(request.endDate()));
    }

    final JPAQuery<Result> query =
        jpaQueryFactory.selectFrom(qr).where(predicate).orderBy(qr.createdAt.desc());

    int limit = request.limit() != null ? request.limit() : DEFAULT_LIMIT;
    query.limit(limit);
    if (request.page() != null) {
      query.offset((long) (request.page() - 1) * limit);
    }

    List<Result> resultModels = query.fetch();

    return resultModels.stream()
        .map(
            r ->
                new MatchDto(
                    r.getId(),
                    r.getMyDeck(),
                    r.getOpponentDeck(),
                    r.isFirst(),
                    r.getTurnCount(),
                    r.getOutcome(),
                    r.getCreatedAt()))
        .toList();
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
}
