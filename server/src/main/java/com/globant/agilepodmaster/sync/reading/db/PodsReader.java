package com.globant.agilepodmaster.sync.reading.db;

import com.globant.agilepodmaster.core.Pod;
import com.globant.agilepodmaster.core.PodMember;
import com.globant.agilepodmaster.core.PodMemberRepository;
import com.globant.agilepodmaster.core.PodRepository;
import com.globant.agilepodmaster.sync.reading.PodsBuilder;
import com.globant.agilepodmaster.sync.reading.Reader;

/**
 * Class that reads Pods and its members for a DB.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public class PodsReader implements Reader<PodsBuilder> {

  PodRepository podRepository;

  PodMemberRepository podMemberRepository;

  /**
   * Constructor.
   * @param podRepository repository for Pods.
   * @param podMemberRepository  repository for Pod Members.
   */
  public PodsReader(PodRepository podRepository,
      PodMemberRepository podMemberRepository) {
    this.podRepository = podRepository;
    this.podMemberRepository = podMemberRepository;
  }

  @Override
  public void readInto(PodsBuilder builder) {

    Iterable<Pod> pods = podRepository.findAll();

    for (Pod pod : pods) {
      builder.addPod(pod.getName());
      Iterable<PodMember> podMembers = podMemberRepository.findByPod(pod);
      for (PodMember podMember : podMembers) {
        builder.addMember(podMember.getFirstName(), podMember.getLastName(),
            podMember.getEmail(), null);
      }
    } 
  }

}
