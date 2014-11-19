package com.globant.agilepodmaster.core;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface MetricDataRepository extends 
    QueryDslPredicateExecutor<MetricData>,
    CrudRepository<MetricData, Long> {
}
