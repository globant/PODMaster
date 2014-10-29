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
public class PodMember extends AbstractEntity {
  
  @NonNull
  public String firstName;
  
  @NonNull
  public String lastName;
  
  @NonNull
  public String email;
  
  @NonNull
  @ManyToOne
  @JsonIgnore
  private Pod pod;
  
}