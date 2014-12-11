package com.globant.agilepodmaster.core;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for AbstractMetric.
 * @author Andres Postiglioni.
 *
 */
public interface AbstractMetricRepository extends 
    CrudRepository<AbstractMetric, Long>,
    QueryDslPredicateExecutor<AbstractMetric> {
}
