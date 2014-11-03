package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class HighlightData implements Serializable {
  
  private String title;

  private String description;

  private Date highlightDate;
  
}
