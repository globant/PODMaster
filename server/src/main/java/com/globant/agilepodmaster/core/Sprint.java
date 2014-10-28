package com.globant.agilepodmaster.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Represents a set of tasks.
 * @author jose.dominguez@globant.com
 *
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class Sprint extends AbstractEntity {
  
  public int number;
  
  @NonNull
  public String name;
  
  @NonNull
  @ManyToOne
  @JsonIgnore
  private Release relase;
  
  public Date startDate;

  public Date endDate;

}