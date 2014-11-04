package com.globant.agilepodmaster.sync.reading;

/**
 * Defines the functionality of a Pod builder.
 * @author jose.dominguez@globant.com
 *
 */
public interface PodsBuilder extends BuilderLog {

  /**
   * Add a Pod data.
   * @param name the name of the pod.
   * @return the same PodsBuilder with the Pod data.
   */
  PodsBuilder addPod(String name);

  /**
   * Add a Pod member.
   * @param firstName the first name. 
   * @param lastName the last name.
   * @param email the email.
   * @param role the role.
   * @return the same PodsBuilder with the member data
   */
  PodsBuilder addMember(String firstName, String lastName, String email,
      String role);

}
