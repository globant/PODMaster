package com.globant.agilepodmaster.pod;

public interface PodBuilder {
  PodBuilder withPod(String string);
  PodBuilder addMember(String name, String role);
}
