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
public class PodMember extends AbstractEntity {
  
  @SuppressWarnings("unused")
  private PodMember() {
    // required by Hibernate
  }
  
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