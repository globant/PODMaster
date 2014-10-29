package com.globant.agilepodmaster.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Pod extends AbstractEntity {
  
  public static enum Type {
    Internal, External, Unassigned
  }
  
  @NonNull
  public String name;
  
  @NonNull
  @ManyToOne
  @JsonIgnore
  private Sprint sprint;
  
  public Type type;

}