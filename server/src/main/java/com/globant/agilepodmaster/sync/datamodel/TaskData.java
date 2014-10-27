package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class TaskData implements Serializable {
  
  public String key;

  public String name;

  public String environment;

  public String component;

  public String activity;

  public String pod;

  public String podMember;

  public float effort;

  public float estimated;

  public float actual;

  public float remaining;

  public String statusCode;

  public String severityCode;

  public String priorityCode;

  public String typeCode;

  public Date createdDate;

  public Date endDate;

  public boolean external;

  public Date fixDate;

  public int reopenCount;

  public List<TaskData> subTasks;

  public TaskData() {
    subTasks = new ArrayList<TaskData>();
  }
  
}   
