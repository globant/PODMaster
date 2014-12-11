package com.globant.agilepodmaster.core;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository of ProjectMetric entity.
 * @author Jose Dominguez
 *
 */
public interface ProjectMetricRepository extends CrudRepository<ProjectMetric, Long>,
    QueryDslPredicateExecutor<ProjectMetric> {
}
