package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;

import lombok.Data;

@Data
public class RiskData implements Serializable {

  public String title;

  public String description;

  public String likelihoodCode;

  public String impactCode;

}
