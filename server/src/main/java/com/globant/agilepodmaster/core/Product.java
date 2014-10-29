package com.globant.agilepodmaster.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Product of an organization. It is composed of one or more projects.
 * @author jose.dominguez@globant.com
 *
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Product extends AbstractEntity {
  
  @NonNull
  public String name;
  
  @NonNull
  @ManyToOne
  @JsonIgnore
  private Organization organization;
  
  public String description;

}
