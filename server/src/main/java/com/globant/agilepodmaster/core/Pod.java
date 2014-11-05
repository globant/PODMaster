package com.globant.agilepodmaster.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Entity that represents a POD.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Pod extends AbstractEntity {
  
  /**
   * Type of POD.
   * @author jose.dominguez@globant.com
   *
   */
  public static enum Type {
    Internal, External, Unassigned
  }
  
  @Getter
  @NonNull
  private String name;
  
  //@NonNull
  @ManyToOne
  @JsonIgnore
  private Sprint sprint;
    
  @NonNull
  private Type type;

}