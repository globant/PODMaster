package com.globant.agilepodmaster.core;

import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class Organization extends AbstractEntity {
  
  @SuppressWarnings("unused")
  private Organization() {
    // required by Hibernate
  }
  
  @NonNull
  public String name;

  public String description;

}
