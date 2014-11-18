package com.globant.agilepodmaster.sync.reading;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO to manage PodMembers configuration from any data source.
 * @author jose.dominguez@globant.com
 */
@Data
@AllArgsConstructor
public class PodMemberDTO {
  
  private String userName;
  
  private String firstName;

  private String lastName;

  private String podName;

}
