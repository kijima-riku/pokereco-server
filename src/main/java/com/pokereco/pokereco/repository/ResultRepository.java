package com.pokereco.pokereco.repository;

import com.pokereco.pokereco.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ResultRepository extends JpaRepository<Result, Long>, QuerydslPredicateExecutor<Result> {
}
