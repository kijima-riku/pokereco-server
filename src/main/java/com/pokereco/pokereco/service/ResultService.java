package com.pokereco.pokereco.service;

import com.pokereco.pokereco.dto.ResultDeckStatsDto;
import com.pokereco.pokereco.dto.ResultRequestDto;
import com.pokereco.pokereco.model.Deck;
import com.pokereco.pokereco.model.QResult;
import com.pokereco.pokereco.model.Result;
import com.pokereco.pokereco.repository.DeckRepository;
import com.pokereco.pokereco.repository.ResultRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
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
    private final int DEFAULT_LIMIT = 15;
    private final short OUTCOME_WIN = 1;

    public ResultService(final JPAQueryFactory jpaQueryFactory, final ResultRepository resultRepository, final DeckRepository deckRepository) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.resultRepository = resultRepository;
        this.deckRepository = deckRepository;
    }

    public List<Result> getResults(Long userId, ResultRequestDto request){
        final QResult qr = QResult.result;
        final BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(qr.user.id.eq(userId));

        System.out.println(request.getOutcome());
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

        return jpaQueryFactory.selectFrom(qr)
                .where(predicate)
                .orderBy(qr.createdAt.desc())
                .limit(Optional.ofNullable(request.getLimit()).orElse(DEFAULT_LIMIT))
                .offset(Optional.ofNullable(request.getPage()).map(p -> (p - 1) * request.getLimit()).orElse(0))
                .fetch();
    }

    public List<ResultDeckStatsDto>getDeckStats(Long userId) {
        QResult qr = QResult.result;
        List<Tuple> deckStats = jpaQueryFactory
                .select(qr.myDeck.id, qr.id.count(), qr.outcome.when(OUTCOME_WIN).then(1L).otherwise(0L).sum())
                .from(qr)
                .where(qr.user.id.eq(userId))
                .groupBy(qr.myDeck.id)
                .fetch();
        List<ResultDeckStatsDto> response = new ArrayList<>();
        for (Tuple record : deckStats){
            Integer deckId = record.get(qr.myDeck.id);
            Long totalMatches = record.get(qr.id.count());
            Long totalWins = record.get(qr.outcome.when((short) 1).then(1L).otherwise(0L).sum());
            totalMatches = (totalMatches != null) ? totalMatches : 0L;
            totalWins = (totalWins != null) ? totalWins : 0L;
            double winRate = (totalMatches > 0 ? (double) totalWins / totalMatches : 0.0);
            response.add(new ResultDeckStatsDto(deckId, totalMatches, winRate * 100));
        }
        return response;
    }
}
