package com.globant.agilepodmaster.sync.reading;

import com.globant.agilepodmaster.sync.OrganizationBuilder;

/**
 * Defines the functionality of a Release builder.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public interface ReleasesBuilder {

  /**
   * Add organization to the Builder.
   * 
   * @param organizationName the name of the organization.
   * @return the same Builder.
   */
  OrganizationBuilder withOrganization(String organizationName);
}
