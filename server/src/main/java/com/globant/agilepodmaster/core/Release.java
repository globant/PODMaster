package com.globant.agilepodmaster.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
@NoArgsConstructor(access=AccessLevel.PRIVATE)
@AllArgsConstructor
public class Release extends AbstractEntity {
  
  public int number;
  
  @NonNull
  public String name;
  
  @NonNull
  @ManyToOne
  @JsonIgnore
  private Snapshot snapshot;
  
  @NonNull
  @ManyToOne
  @JsonIgnore
  private Product product;
  
  public Date creationDate;

}