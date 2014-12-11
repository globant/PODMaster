package com.globant.agilepodmaster.product;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository of Organization entity.
 * @author jose.dominguez@globant.com
 *
 */
public interface OrganizationRepository extends CrudRepository<Organization, Long> {
  
  /**
   * @param name name of the organization.
   * @return a list of organizations.
   */
  List<Organization> findByName(String name);

  List<Organization> findBySnapshotId(Long snapshotId);
}
