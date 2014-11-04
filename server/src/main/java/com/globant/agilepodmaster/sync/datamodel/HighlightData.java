package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * DTO that represents a highlight taken from a data source.
 * This was taken for AgileTracker. It will be possibly remove.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Data
public class HighlightData implements Serializable {
  
  private String title;

  private String description;

  private Date highlightDate;
  
}
