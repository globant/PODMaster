package com.globant.agilepodmaster.core;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@ToString
public class Release extends SnapshotEntity {
  
  private int number;
  
  @NonNull
  private String name;
  
//  @NonNull
//  @ManyToOne
//  @JsonIgnore
//  @Setter(AccessLevel.PACKAGE)
//  private Snapshot snapshot;
  
  @NonNull
  @ManyToOne
  @JsonIgnore
  private Project project;

  /**
   * Constructor.
   * @param name the release name.
   * @param snapshot the snapshot of this release.
   * @param project the project of this release.
   */
  public Release(String name, Project project) {
    super();
    Assert.notNull(name, "name cannot be null");
    Assert.notNull(project, "project cannot be null");
    this.name = name;
    this.project = project;
  }
}