package com.globant.agilepodmaster.core;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Project of a Product. 
 * @author jose.dominguez@globant.com
 *
 */
@Entity
@ToString
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Project extends SnapshotEntity {
  
  @Getter
  @Setter
  @NonNull
  private String name;
  
  @Getter
  @NonNull
  @ManyToOne
  @JsonIgnore
  private Product product;
  
  @Setter
  @Getter
  private String description;
}