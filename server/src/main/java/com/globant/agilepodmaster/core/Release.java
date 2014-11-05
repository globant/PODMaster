package com.globant.agilepodmaster.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.util.Assert;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * A Release groups sprints and a backlog. It is for just one project and one
 * snapshot.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Release extends AbstractEntity {
  
  private int number;
  
  @NonNull
  private String name;
  
  @NonNull
  @ManyToOne
  @JsonIgnore
  private Snapshot snapshot;
  
  @NonNull
  @ManyToOne
  @JsonIgnore
  private Project project;
  
  private Date creationDate;

  /**
   * Constructor.
   * @param name the release name.
   * @param snapshot the snapshot of this release.
   * @param project the project of this release.
   * @param creationDate the creation date of the release.
   */
  public Release(String name, Snapshot snapshot, Project project,
      Date creationDate) {
    super();
    Assert.notNull(name, "name cannot be null");
    Assert.notNull(snapshot, "snapshot cannot be null");
    Assert.notNull(project, "project cannot be null");
    Assert.notNull(creationDate, "creationDate cannot be null");
    this.name = name;
    this.snapshot = snapshot;
    this.project = project;
    this.creationDate = new Date(creationDate.getTime());
  }
  
  

}