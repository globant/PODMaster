package com.globant.agilepodmaster.sync.datamodel;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO that represents a sprint taken from a data source.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@EqualsAndHashCode(exclude = { "startDate", "endDate" })
@Data
public class SprintData implements Serializable {
  
  private int number;

  private String name;

  private String tag;

  private List<TaskData> sprintTasks;
  
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE) 
  private Date startDate;
  
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE) 
  private Date endDate;

  /**
   * Constructor.
   * 
   * @param name the name of the sprint.
   * @param number the number of the sprint.
   * @param startDate the start date.
   * @param endDate the end date.
   */
  public SprintData(final String name, final int number, final Date startDate,
      final Date endDate) {
    Assert.notNull(startDate, "startDate cannot be null");
    Assert.notNull(endDate, "endDate cannot be null");
    this.name = name;
    this.number = number;
    this.startDate = new Date(startDate.getTime());
    this.endDate = new Date(endDate.getTime());
    sprintTasks = new ArrayList<TaskData>();
  }
  
  /**
   * startDate getter.
   * @return the date.
   */
  public Date getStartDate() {
    return new Date(startDate.getTime());
  }

  /**
   * endDate getter.
   * @return the date.
   */
  public Date getEndDate() {
    return new Date(endDate.getTime());
  }
  
}
