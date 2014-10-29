package com.globant.agilepodmaster.sync.datamodel;

import com.globant.agilepodmaster.sync.datamodel.PodData.PodMemberData;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * DTO to move all data taken from the data sources to import process.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Data
@RequiredArgsConstructor
public class ProjectDataSet implements Serializable {

  public SnapshotData snapshot;

  public List<NonWorkingDaysData> nonWorkingDays;

  @NonNull
  public List<PodData> pods;

  @NonNull
  public List<ReleaseData> releases;



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
