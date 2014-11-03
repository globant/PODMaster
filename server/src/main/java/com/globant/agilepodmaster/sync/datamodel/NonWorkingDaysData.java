package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class NonWorkingDaysData implements Serializable {
  
  private String pod;

  private String podMemberEmail;

  private Date offWorkDate;
  
}
