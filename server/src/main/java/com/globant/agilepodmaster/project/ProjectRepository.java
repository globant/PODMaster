package com.globant.agilepodmaster.project;

import com.globant.agilepodmaster.product.Product;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository of Project entity.
 * @author jose.dominguez@globant.com
 *
 */
public interface ProjectRepository extends CrudRepository<Project, Long> {
  
  /**
   * @param product product whose projects are being searched.
   * @return a list of releases.
   */
  List<Project> findByProduct(Product product);

}
