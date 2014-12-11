package com.globant.agilepodmaster.pod;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository of Pod entity.
 * @author jose.dominguez@globant.com
 *
 */
public interface PodRepository extends CrudRepository<Pod, Long> {
  
  /**
   * @param name name od the pod being searched.
   * @return a list of pods.
   */
  List<Pod> findByName(String name);
}
