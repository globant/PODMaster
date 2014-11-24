package com.globant.agilepodmaster.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.util.Assert;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * Represents a set of tasks.
 * @author jose.dominguez@globant.com
 *
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Sprint extends SnapshotEntity {
  public int number;
  
  @NonNull
  @Getter
  private String name;
  
  @NonNull
  @ManyToOne
  @JsonIgnore
  @Getter
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
    Assert.notNull(name, "name must not be null");
    Assert.notNull(release, "release must not be null");
    Assert.notNull(startDate, "startDate must not be null");
    Assert.notNull(endDate, "endDate must not be null");
    this.name = name;
    this.release = release;
    this.startDate = new Date(startDate.getTime());
    this.endDate = new Date(endDate.getTime());
  }

  public Date getStartDate() {
    //TODO: Use java8 localdate to avoid this uglyness
    return cloneDate(startDate);
  }
  
  public Date getEndDate() {
    //TODO: Use java8 localdate to avoid this uglyness
    return cloneDate(endDate);
  }

  private Date cloneDate(Date date) {
    return date != null
           ? (Date) date.clone()
           : null;
  }
}