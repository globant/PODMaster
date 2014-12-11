package com.globant.agilepodmaster.snapshot;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository of ProjectPodMetric entity.
 * @author Andres Postiglioni
 *
 */
public interface ProjectPodMetricRepository extends CrudRepository<ProjectPodMetric, Long>,
    QueryDslPredicateExecutor<ProjectPodMetric> {
}
