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

  public String name;

  public String officeName;

  public PodTypeData podType;

  public List<PodMemberData> podMembers;

  public PodData(PodTypeData podType) {
    this.podType = podType;
    podMembers = new ArrayList<PodMemberData>();
  }


  @Data
  public class PodMemberData implements Serializable {
    
    public String firstName;

    public String lastName;

    public String email;

    public String externalUsername;

    public String officeName;

    public String roleCode;

    public float dailyHours;

    public float hourlyRate;

    public Date startDate;

    public Date endDate;
  }
}
