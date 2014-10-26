package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SprintData implements Serializable {
  
  public int number;

  public String name;

  public Date startDate;

  public Date endDate;

  public float progress;

  public String tag;

  public List<TaskData> sprintTasks;

  public SprintData(final String name, final int number, final Date startDate, final Date endDate,
      final float progress) {
    this.name = name;
    this.number = number;
    this.startDate = startDate;
    this.endDate = endDate;
    this.progress = progress;
    sprintTasks = new ArrayList<TaskData>();
  }
  
}
