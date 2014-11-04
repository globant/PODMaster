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

  private SnapshotData snapshot;

  private List<NonWorkingDaysData> nonWorkingDays;

  @NonNull
  private List<PodData> pods;

  @NonNull
  private List<ReleaseData> releases;



  /**
   * Search in this DTO for a pod member by user name.
   * @param username the user name.
   * @return a PodMember object or null.
   */
  public PodMemberData getPodMemberByUsername(String username) {
    PodMemberData podMemberResult = null;
    for (PodData pod : pods) {
      for (PodMemberData podMember : pod.getPodMembers()) {
        if (podMember.getExternalUsername().equalsIgnoreCase(username)) {
          podMemberResult = podMember;
          break;
        }
      }
    }
    return podMemberResult;
  }

  /**
   * Search in this DTO for a pod  by user name.
   * @param username the user name.
   * @return a Pod object or null.
   */
  public PodData getPodByUsername(String username) {
    PodData podResult = null;
    for (PodData pod : pods) {
      for (PodMemberData podMember : pod.getPodMembers()) {
        if (podMember.getExternalUsername().equalsIgnoreCase(username)) {
          podResult = pod;
          break;
        }
      }
    }
    return podResult;
  }

  /**
   * Search in this DTO for a pod  by pod name.
   * @param podName the pod name.
   * @return a Pod object or null.
   */
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

  /**
   * Search in this DTO for a pod  by type.
   * @param type the pod type.
   * @return a Pod object or null.
   */
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
