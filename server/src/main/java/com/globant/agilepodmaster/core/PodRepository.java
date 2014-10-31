package com.globant.agilepodmaster.core;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

public interface PodRepository extends CrudRepository<Pod, Long> {
}
