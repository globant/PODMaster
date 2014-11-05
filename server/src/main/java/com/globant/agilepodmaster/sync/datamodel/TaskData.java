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
 * DTO that represents a task taken from a data source.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@EqualsAndHashCode(exclude = { "createDate", "endDate", "fixDate" })
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

  private boolean external;

  private int reopenCount;

  private List<TaskData> subTasks;
  
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE) 
  private Date createDate;
  
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE) 
  private Date endDate;
  
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE) 
  private Date fixDate;

  /**
   * Constructor.
   */
  public TaskData() {
    subTasks = new ArrayList<TaskData>();
  }
  


  /**
   * createDate getter.
   * @return the date.
   */
  public Date getCreateDate() {
    return new Date(createDate.getTime());
  }

  /**
   * createDate setter.
   * @param createDate the date.
   */
  public void setCreateDate(Date createDate) {
    Assert.notNull(createDate, "createDate is null");
    this.createDate = new Date(createDate.getTime());
  }

  /**
   * endDate getter.
   * @return the date.
   */
  public Date getEndDate() {
    return new Date(endDate.getTime());
  }

  /**
   * endDate setter.
   * @param endDate the date.
   */
  public void setEndDate(Date endDate) {
    Assert.notNull(endDate, "endDate is null");
    this.endDate = new Date(endDate.getTime());
  }


  /**
   * fixDate getter.
   * @return the date.
   */
  public Date getFixDate() {
    return new Date(fixDate.getTime());
  }

  /**
   * fixDate setter.
   * @param fixDate the date.
   */
  public void setFixDate(Date fixDate) {
    Assert.notNull(fixDate, "fixDate is null");
    this.fixDate = new Date(fixDate.getTime());
  }
  
}   
