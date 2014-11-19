package com.globant.agilepodmaster.core;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Product of an organization. It is composed of one or more projects.
 * @author jose.dominguez@globant.com
 *
 */
@Entity
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Product extends SnapshotEntity {
  @NonNull
  private String name;
  
  @NonNull
  @ManyToOne
  @JsonIgnore
  private Organization organization;
  
  @Setter
  private String description;
}
