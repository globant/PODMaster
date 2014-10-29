package com.globant.agilepodmaster.sync.reading;

public interface PodsBuilder extends BuilderLog{
  
  PodsBuilder addPod(String name);

  PodsBuilder addMember(String name, String role);

}
