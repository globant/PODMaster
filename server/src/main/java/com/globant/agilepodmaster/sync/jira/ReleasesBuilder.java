package com.globant.agilepodmaster.sync.jira;

import com.globant.agilepodmaster.organization.OrganizationBuilder;

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
