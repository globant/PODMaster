package com.globant.agilepodmaster.core;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Snapshot represents a set of data read from different data sources through
 * the synchronization process. It is associated to a product.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Entity
@EqualsAndHashCode(callSuper = false, exclude = { "creationDate", "sprintMetrics" })
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Snapshot extends AbstractEntity {

  @NonNull
  private String name;

  @NonNull
  @ManyToOne
  @JsonIgnore
  @Setter
  @Getter
  private Product product;

  @Setter(AccessLevel.NONE)
  private Date creationDate;

//  @OneToMany(cascade = {CascadeType.ALL})
  @Getter
  @Transient
  private Set<SprintPodMetric> sprintMetrics = new HashSet<SprintPodMetric>();

  /**
   * Constructor.
   * 
   * @param name the name of the snapshot.
   */
  public Snapshot(String name) {
    
    super();
    this.name = name;
  }

  /**
   * creationDate getter.
   * 
   * @return the date.
   */
  public Date getCreationDate() {
    return new Date(creationDate.getTime());
  }

  /**
   * creationDate setter.
   * 
   * @param creationDate the date.
   */
  public void setCreationDate(Date creationDate) {
    Assert.notNull(creationDate, "creationDate is null");
    this.creationDate = new Date(creationDate.getTime());
  }

  public void addSprintMetric(SprintPodMetric spm) {
    this.sprintMetrics.add(spm);
  }
}
