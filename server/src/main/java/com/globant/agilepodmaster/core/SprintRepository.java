package com.globant.agilepodmaster.core;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository of Sprint entity.
 * @author jose.dominguez@globant.com
 *
 */
public interface SprintRepository extends CrudRepository<Sprint, Long> {

  /**
   * @param release release whose sprints are being searched.
   * @return a list of sprints.
   */
  List<Sprint> findByRelease(Release release);

}
