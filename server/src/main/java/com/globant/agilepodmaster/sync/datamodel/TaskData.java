package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * DTO that represents a task taken from a data source.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Data
public class TaskData implements Serializable {
  
  private String key;

  private String name;

  private String environment;

  private String component;

  private String activity;

  private String pod;

  private String podMember;

  private float effort;

  private float estimated;

  private float actual;

  private float remaining;

  private String statusCode;

  private String severityCode;

  private String priorityCode;

  private String typeCode;

  private Date createdDate;

  private Date endDate;

  private boolean external;

  private Date fixDate;

  private int reopenCount;

  private List<TaskData> subTasks;

  /**
   * Constructor.
   */
  public TaskData() {
    subTasks = new ArrayList<TaskData>();
  }
  
}   
