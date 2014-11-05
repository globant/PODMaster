package com.globant.agilepodmaster.sync.datamodel;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO that represents a member of a pod taken from a data source.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@EqualsAndHashCode(exclude = { "startDate", "endDate" })
@Data
public class PodMemberData implements Serializable {

  private String firstName;

  private String lastName;

  private String email;

  private String externalUsername;

  private String roleCode;

  private float dailyHours;

  public float hourlyRate;
  
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE) 
  private Date startDate;

  /**
   * startDate getter.
   * @return the date.
   */
  public Date getStartDate() {
    return new Date(startDate.getTime());
  }

  /**
   * startDate setter.
   * @param startDate the date.
   */
  public void setStartDate(Date startDate) {
    Assert.notNull(startDate, "startDate is null");
    this.startDate = new Date(startDate.getTime());
  }

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE) 
  private Date endDate;

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
 
  
}
