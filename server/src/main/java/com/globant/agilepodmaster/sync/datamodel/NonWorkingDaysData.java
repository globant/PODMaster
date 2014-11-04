package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * DTO that represents non working days taken from a data source.
 * This was taken for AgileTracker. It will be possibly remove.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Data
public class NonWorkingDaysData implements Serializable {
  
  private String pod;

  private String podMemberEmail;

  private Date offWorkDate;
  
}
