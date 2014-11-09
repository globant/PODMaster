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
import lombok.ToString;

/**
 * Entity that represents a POD.
 * 
 * @author jose.dominguez@globant.com
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@ToString
public class Pod extends AbstractEntity {
  
  @Getter
  @NonNull
  private String name;
  
  @NonNull
  @ManyToOne
  @JsonIgnore
  @Getter
  private Sprint sprint;


}