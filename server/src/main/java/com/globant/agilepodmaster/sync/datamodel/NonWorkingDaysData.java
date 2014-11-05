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
 * DTO that represents non working days taken from a data source.
 * This was taken for AgileTracker. It will be possibly remove.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Data
@EqualsAndHashCode(exclude = { "offWorkDate" })
public class NonWorkingDaysData implements Serializable {
  
  private String pod;

  private String podMemberEmail;
  
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE) 
  private Date offWorkDate;

  /**
   * offWorkDate getter.
   * @return the date.
   */
  public Date getOffWorkDate() {
    return new Date(offWorkDate.getTime());
  }

  /**
   * offWorkDate setter.
   * @param offWorkDate the date.
   */
  public void setOffWorkDate(Date offWorkDate) {
    Assert.notNull(offWorkDate, "offWorkDate is null");
    this.offWorkDate = new Date(offWorkDate.getTime());
  }
  
}
