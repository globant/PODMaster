package com.globant.agilepodmaster.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Snapshot represents a set of data read from different data sources through
 * the synchronization process. It is associated to a product.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Snapshot extends AbstractEntity {

  @NonNull
  public String name;

  @NonNull
  @ManyToOne
  @JsonIgnore
  private Product product;

  @NonNull
  public Date creationDate;

}
