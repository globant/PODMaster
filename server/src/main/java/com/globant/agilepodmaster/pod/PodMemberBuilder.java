package com.globant.agilepodmaster.pod;

import com.globant.agilepodmaster.snapshot.SnapshotBuilder;
import com.globant.agilepodmaster.snapshot.SnapshotDataCollector;

public class PodMemberBuilder implements SnapshotDataCollector {
  private final PodMember podMember;
  private final PodBuilder podBuilder;

  public PodMemberBuilder(String fistname, String lastname, String email, Pod pod,
      PodBuilder podBuilder) {
    this.podBuilder = podBuilder;
    podMember = new PodMember(fistname, lastname, email, pod);
  }

  @Override
  public void collect(SnapshotBuilder snapshotBuilder) {
    snapshotBuilder.addPodMember(podMember);
  }

  public PodBuilder addToPod() {
    return podBuilder;
  }
}
