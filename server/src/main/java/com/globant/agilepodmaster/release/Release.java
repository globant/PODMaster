package com.globant.agilepodmaster.release;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.globant.agilepodmaster.project.Project;
import com.globant.agilepodmaster.snapshot.SnapshotEntity;

import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

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
public class Release extends SnapshotEntity implements Comparable<Release> {

  private int number;
  
  @NonNull
  @Getter
  private String name;
  
  @NonNull
  @ManyToOne
  @JsonIgnore
  @Getter
  private Project project;

  /**
   * Constructor.
   * @param name the release name.
   * @param project the project of this release.
   */
  public Release(String name, Project project) {
    super();
    Assert.notNull(name, "name cannot be null");
    Assert.notNull(project, "project cannot be null");
    this.name = name;
    this.project = project;
  }
  
  @Override
  public int compareTo(Release otherRelease) {
    return this.hashCode() - otherRelease.hashCode();
  }
  
}