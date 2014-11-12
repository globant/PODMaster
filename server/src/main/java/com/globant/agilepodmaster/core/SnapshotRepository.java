package com.globant.agilepodmaster.core;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository of Snapshot entity.
 * @author jose.dominguez@globant.com
 *
 */
public interface SnapshotRepository extends CrudRepository<Snapshot, Long>, QueryDslPredicateExecutor<Snapshot> {


}
