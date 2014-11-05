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
 * DTO that represents a highlight taken from a data source.
 * This was taken for AgileTracker. It will be possibly remove.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@EqualsAndHashCode(exclude = { "highlightDate" })
@Data
public class HighlightData implements Serializable {
  
  private String title;

  private String description;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE) 
  private Date highlightDate;

  /**
   * highlightDate getter.
   * @return the date.
   */
  public Date getHighlightDate() {
    return new Date(highlightDate.getTime());
  }

  /**
   * highlightDate setter.
   * @param highlightDate the date.
   */
  public void setHighlightDate(Date highlightDate) {
    Assert.notNull(highlightDate, "highlightDate is null");
    this.highlightDate = new Date(highlightDate.getTime());
  }
  

}
