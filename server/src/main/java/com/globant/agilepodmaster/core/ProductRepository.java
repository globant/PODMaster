package com.globant.agilepodmaster.core;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository of Product entity.
 * @author jose.dominguez@globant.com
 *
 */
public interface ProductRepository extends CrudRepository<Product, Long> {

}
