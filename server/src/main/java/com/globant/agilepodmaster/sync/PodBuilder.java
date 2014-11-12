package com.globant.agilepodmaster.sync;

import com.globant.agilepodmaster.core.Pod;

public class PodBuilder extends AbstractBuilder<SnapshotBuilder, PodMemberBuilder>{
  private final Pod pod;

  public PodBuilder(String name, SnapshotBuilder snapshotBuilder) {
    super(snapshotBuilder);

    pod = new Pod(name);
  }

  public PodMemberBuilder withPodMember(String fistname, String lastname, String email) {
    PodMemberBuilder nestedBuilder = new PodMemberBuilder(fistname, lastname, email, pod, this);
    this.addNestedBuilder(nestedBuilder);
    return nestedBuilder;
  }

  @Override
  protected void doCollect(SnapshotBuilder snapshotBuilder) {
    snapshotBuilder.addPod(pod);
  }

  public SnapshotBuilder addToSnapshot() {
    return this.getParentBuilder();
  }
}
