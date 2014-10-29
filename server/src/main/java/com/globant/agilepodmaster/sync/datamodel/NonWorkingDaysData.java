package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class NonWorkingDaysData implements Serializable {
  
  public String pod;

  public String podMemberEmail;

  public Date offWorkDate;
  
}
