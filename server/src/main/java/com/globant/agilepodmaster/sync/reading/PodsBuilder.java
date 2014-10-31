package com.globant.agilepodmaster.sync.reading;

public interface PodsBuilder extends BuilderLog {

  PodsBuilder addPod(String name);

  PodsBuilder addMember(String firstName, String lastName, String email,
      String role);

}
