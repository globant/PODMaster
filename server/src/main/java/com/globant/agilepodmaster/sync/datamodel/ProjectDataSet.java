package com.globant.agilepodmaster.sync.datamodel;

import com.globant.agilepodmaster.sync.datamodel.PodData.PodMemberData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * DTO to move all data taken from the data sources to import process.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Data
public class ProjectDataSet implements Serializable {

  public SnapshotData snapshot;

  public List<NonWorkingDaysData> nonWorkingDays;

  public List<PodData> pods;

  public List<ReleaseData> releases;

  /**
   * Constructor.
   */
  public ProjectDataSet() {
    nonWorkingDays = new ArrayList<NonWorkingDaysData>();
    pods = new ArrayList<PodData>();
    releases = new ArrayList<ReleaseData>();
  }
  
  //util methods
 
  public PodMemberData getPodMemberByUsername(String externalUsername) {
    PodMemberData podMemberResult = null;
    for (PodData pod : pods) {
      for (PodMemberData podMember : pod.getPodMembers()) {
        if (podMember.getExternalUsername().equalsIgnoreCase(externalUsername)) {
          podMemberResult = podMember;
          break;
        }
      }
    }
    return podMemberResult;
  }

  public PodData getPodByUsername(String externalUsername) {
    PodData podResult = null;
    for (PodData pod : pods) {
      for (PodMemberData podMember : pod.getPodMembers()) {
        if (podMember.getExternalUsername().equalsIgnoreCase(externalUsername)) {
          podResult = pod;
          break;
        }
      }
    }
    return podResult;
  }

  public PodData getPodByName(String podName) {
    PodData podResult = null;
    for (PodData pod : pods) {
      if (pod.getName().equalsIgnoreCase(podName)) {
        podResult = pod;
        break;
      }
    }
    return podResult;
  }

  public PodData getOrCreatePod(PodData.PodTypeData type) {
    PodData podResult = null;
    for (PodData pod : pods) {
      if (pod.getPodType() == type) {
        podResult = pod;
        break;
      }
    }

    if (podResult == null) {
      podResult = new PodData(type);
      pods.add(podResult);
    }
    return podResult;
  }

}
