package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * DTO that represents a sprint taken from a data source.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Data
public class SprintData implements Serializable {
  
  private int number;

  private String name;

  private Date startDate;

  private Date endDate;

  private String tag;

  private List<TaskData> sprintTasks;

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
    this.name = name;
    this.number = number;
    this.startDate = startDate;
    this.endDate = endDate;
    sprintTasks = new ArrayList<TaskData>();
  }
  
}
