package com.globant.agilepodmaster.pod;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository of PodMember entity.
 * @author jose.dominguez@globant.com
 *
 */
public interface PodMemberRepository extends CrudRepository<PodMember, Long> {

  /**
   * @param pod pod whose pod members are being searched.
   * @return a list of pod members
   */
  List<PodMember> findByPod(Pod pod);
}
