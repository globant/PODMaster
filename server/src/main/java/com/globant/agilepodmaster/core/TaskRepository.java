package com.globant.agilepodmaster.core;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository of Task entity.
 * @author jose.dominguez@globant.com
 *
 */
public interface TaskRepository extends CrudRepository<Task, Long> {
  
  /**
   * @param release release whose tasks are being searched.
   * @param sprint sprint whose tasks are being searched.
   * @return a list of tasks.
   */
  List<Task> findByReleaseAndSprint(Release release, Sprint sprint);  
  

}
