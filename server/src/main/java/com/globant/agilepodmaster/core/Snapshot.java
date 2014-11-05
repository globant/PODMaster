package com.globant.agilepodmaster.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
public class Snapshot extends AbstractEntity {

  @NonNull
  private String name;

  @NonNull
  @ManyToOne
  @JsonIgnore
  private Product product;

  @NonNull
  private Date creationDate;

  /**
   * Constructor.
   * @param name the name of the snapshot.
   * @param product the product of the snapshot.
   * @param creationDate the creation date of the snapshot.
   */
  public Snapshot(String name, Product product, Date creationDate) {
    super();
    this.name = name;
    this.product = product;
    this.creationDate = new Date(creationDate.getTime());
  }

  
  
}
