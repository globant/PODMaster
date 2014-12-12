package com.globant.agilepodmaster.pod;


/**
 * Defines the functionality of a Pod builder.
 * @author jose.dominguez@globant.com
 *
 */
public interface PodsBuilder {
  
  /**
   * Add pod to builder.
   * @param name name of the Pod.
   * @return the same PodBuilder.
   */
  PodBuilder withPod(String name);

}
