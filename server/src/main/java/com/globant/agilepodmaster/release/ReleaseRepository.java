package com.globant.agilepodmaster.release;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository of Release entity.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public interface ReleaseRepository extends CrudRepository<Release, Long>,
    QueryDslPredicateExecutor<Release> {

  // /**
  // * @param snapshot snapshot whose release are being searched.
  // * @return a list of releases.
  // */
  // List<Release> findBySnapshot(Snapshot snapshot);
}
