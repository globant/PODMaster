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
public class Sprint extends SnapshotEntity implements Comparable<Sprint> {

  @NonNull
  @Getter
  private String name;
  
  @Getter
  public int number;
  
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
   * @param number the number of the sprint. 
   * @param release the release of the sprint.
   * @param startDate the start date of the sprint.
   * @param endDate the end date of the sprint.
   */
  public Sprint(String name, int number, Release release, Date startDate, Date endDate) {
    super();
    Assert.notNull(name, "name must not be null");
    Assert.notNull(release, "release must not be null");
    Assert.notNull(startDate, "startDate must not be null");
    Assert.notNull(endDate, "endDate must not be null");
    this.name = name;
    this.number = number;
    this.release = release;
    this.startDate = new Date(startDate.getTime());
    this.endDate = new Date(endDate.getTime());
  }

  /**
   * Return the sprint start date.
   * @return the sprint start date.
   */
  public Date getStartDate() {
    //TODO: Use java8 localdate to avoid this uglyness
    return cloneDate(startDate);
  }
  
  /**
   * Return the sprint end date.
   * @return the sprint end date.
   */
  public Date getEndDate() {
    //TODO: Use java8 localdate to avoid this uglyness
    return cloneDate(endDate);
  }

  private Date cloneDate(Date date) {
    return date != null
           ? (Date) date.clone()
           : null;
  }
  
  @Override
  public int compareTo(Sprint otherSprint) {
    if (this.release.equals(otherSprint.getRelease())) {
      return this.number - otherSprint.getNumber();
    }
    return this.release.compareTo(otherSprint.getRelease());
  }
  
}