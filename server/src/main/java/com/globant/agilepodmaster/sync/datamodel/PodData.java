package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * DTO that represents a POD taken from a data source.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Data
public class PodData implements Serializable {

  /**
   * Kind of Pods.
   */
  public static enum PodTypeData {
    INTERNAL, EXTERNAL, UNASSIGNED
  }

  private String name;

  private String officeName;

  private PodTypeData podType;

  private List<PodMemberData> podMembers;

  /**
   * Constructor.
   * 
   * @param podType type of POD.
   */
  public PodData(PodTypeData podType) {
    this.podType = podType;
    podMembers = new ArrayList<PodMemberData>();
  }


}
