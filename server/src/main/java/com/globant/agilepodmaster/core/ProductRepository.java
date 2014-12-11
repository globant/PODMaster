package com.globant.agilepodmaster.core;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository of Product entity.
 * @author jose.dominguez@globant.com
 *
 */
public interface ProductRepository extends CrudRepository<Product, Long> {
  
  /**
   * @param organization organization whose product I search..
   * @return a list of products.
   */
  List<Product> findByOrganization(Organization organization);

}
