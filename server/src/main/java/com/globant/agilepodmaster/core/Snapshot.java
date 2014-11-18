package com.globant.agilepodmaster.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
@EqualsAndHashCode(callSuper = false, of = { "name" })
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

  @Getter
  @OneToMany(cascade = { CascadeType.ALL })
  private List<Pod> pods = new ArrayList<Pod>();
  
  @Getter
  @OneToMany(cascade = { CascadeType.ALL })
  private List<PodMember> podMembers  = new ArrayList<PodMember>();

  @Getter
  @OneToMany(cascade = { CascadeType.ALL })
  private List<Release> releases = new ArrayList<Release>();
  
  @Getter
  @OneToMany(cascade = { CascadeType.ALL })
  private List<Sprint> sprints = new ArrayList<Sprint>();
  
  @Getter
  @OneToMany(cascade = { CascadeType.ALL })
  private List<Task> tasks = new ArrayList<Task>();

  @Getter
  @OneToMany(cascade = { CascadeType.ALL })
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
