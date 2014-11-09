package com.globant.agilepodmaster.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Represents a set of tasks.
 * @author jose.dominguez@globant.com
 *
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Sprint extends AbstractEntity {
  
  public int number;
  
  @NonNull
  @Getter
  private String name;
  
  @NonNull
  @ManyToOne
  @JsonIgnore
  private Release release;
  
  private Date startDate;

  private Date endDate;

  /**
   * Constructor.
   * @param name the name of the sprint.
   * @param release the release of the sprint.
   * @param startDate the start date of the sprint.
   * @param endDate the end date of the sprint.
   */
  public Sprint(String name, Release release, Date startDate, Date endDate) {
    super();
    this.name = name;
    this.release = release;
    this.startDate = new Date(startDate.getTime());
    this.endDate = new Date(endDate.getTime());
  }
  
  

}