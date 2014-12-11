package com.globant.agilepodmaster.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.globant.agilepodmaster.product.Product;
import com.globant.agilepodmaster.snapshot.SnapshotEntity;

import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * Project of a Product. 
 * @author jose.dominguez@globant.com
 *
 */
@Entity
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Project extends SnapshotEntity {
  
  @Setter
  @NonNull
  @Getter
  private String name;
  
  @Getter
  private int plannedStoryPoints;
  
  @Getter
  @NonNull
  @ManyToOne
  @JsonIgnore
  private Product product;
  
  @Setter
  @Getter
  private String description;
  
  /**
   * Constructor.
   * @param name the name of the project.
   * @param plannedStoryPoints the plannedStoryPoints. 
   * @param product the product of the project.
   */
  public Project(String name, int plannedStoryPoints, Product product) {
    super();
    Assert.notNull(name, "name must not be null");
    Assert.notNull(product, "product must not be null");
    this.name = name;
    this.plannedStoryPoints = plannedStoryPoints;
    this.product = product;
  }
  
}