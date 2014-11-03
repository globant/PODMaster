package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;

import lombok.Data;

@Data
public class RiskData implements Serializable {

  private String title;

  private String description;

  private String likelihoodCode;

  private String impactCode;

}
