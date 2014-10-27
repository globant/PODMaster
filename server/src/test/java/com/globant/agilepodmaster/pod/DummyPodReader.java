package com.globant.agilepodmaster.pod;

public class DummyPodReader implements PodReader {
  @Override
  public void readInto(PodBuilder builder) {
    builder.withPod("pod name")
      .addMember("Name", "Role")
      .addMember("Other member", "other role");
  }
}
