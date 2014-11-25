package com.globant.agilepodmaster.core;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository of SprintPodMetric entity.
 * @author Andres Postiglioni.
 *
 */
public interface SprintPodMetricRepository extends 
    QueryDslPredicateExecutor<SprintPodMetric>, 
    CrudRepository<SprintPodMetric, Long> {
}
