package com.globant.agilepodmaster.core;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository of Release entity.
 * @author jose.dominguez@globant.com
 *
 */
public interface ReleaseRepository extends CrudRepository<Release, Long> {

  /**
   * @param snapshot snapshot whose release are being searched.
   * @return a list of releases.
   */
  List<Release> findBySnapshot(Snapshot snapshot);
}
