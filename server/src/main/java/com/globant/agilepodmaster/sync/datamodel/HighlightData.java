package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class HighlightData implements Serializable {
  
  public String title;

  public String description;

  public Date highlightDate;
  
}
