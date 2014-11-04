package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;

import lombok.Data;

/**
 * DTO that represents a risk taken from a data source.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Data
public class RiskData implements Serializable {

  private String title;

  private String description;

  private String likelihoodCode;

  private String impactCode;

}
