package com.globant.agilepodmaster.sync.reading;

import java.util.Map;

/**
 * Defines the functionality of a Pod builder.
 * @author jose.dominguez@globant.com
 *
 */
public interface PodsBuilder {

  /**
   * Add information of Members and Pods taken from app configuration.
   * @param podMembersMap Map whose key is the user name and value is a MemberDTO.
   * @return this snapshotBuilder
   */
  PodsBuilder addMembersMap(Map<String, PodMemberDTO> podMembersMap);



}
