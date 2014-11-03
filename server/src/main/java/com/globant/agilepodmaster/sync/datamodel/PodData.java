package com.globant.agilepodmaster.sync.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class PodData implements Serializable {

  public static enum PodTypeData {
    INTERNAL, EXTERNAL, UNASSIGNED
  }

  private String name;

  private String officeName;

  private PodTypeData podType;

  private List<PodMemberData> podMembers;

  public PodData(PodTypeData podType) {
    this.podType = podType;
    podMembers = new ArrayList<PodMemberData>();
  }


  @Data
  public class PodMemberData implements Serializable {
    
    private String firstName;

    private String lastName;

    private String email;

    private String externalUsername;

    private String officeName;

    private String roleCode;

    private float dailyHours;

    public float hourlyRate;

    public Date startDate;

    public Date endDate;
  }
}
