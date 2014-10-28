package com.globant.agilepodmaster.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class Pod extends AbstractEntity {
  
  public static enum Type {
    Internal, External, Unassigned
  }
  
  @SuppressWarnings("unused")
  private Pod() {
    // required by Hibernate
  }
  
  @NonNull
  public String name;
  
  @NonNull
  @ManyToOne
  @JsonIgnore
  private Snapshot snapshot;
  
  public Type type;

}