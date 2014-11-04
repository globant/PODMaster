package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * DTO that represents a snapshot taken from a data source.
 * This was taken for AgileTracker. It will be possibly remove.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Data
public class SnapshotData implements Serializable {
  
  private Date startDate;

  private Date endDate;

  private String summary;

  private String mainClientContactName;

  private String mainClientContactEmail;
     
}
