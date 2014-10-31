package com.globant.agilepodmaster.core;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

public interface PodMemberRepository extends CrudRepository<PodMember, Long> {

  List<PodMember> findByPod(Pod pod);
}
