package com.globant.agilepodmaster.core;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ProjectPodMetricRepository extends CrudRepository<ProjectPodMetric, Long>,
    QueryDslPredicateExecutor<ProjectPodMetric> {
}
