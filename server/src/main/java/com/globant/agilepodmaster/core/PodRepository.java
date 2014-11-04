package com.globant.agilepodmaster.core;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository of Pod entity.
 * @author jose.dominguez@globant.com
 *
 */
public interface PodRepository extends CrudRepository<Pod, Long> {
}
