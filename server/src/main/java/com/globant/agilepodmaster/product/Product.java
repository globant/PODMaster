package com.globant.agilepodmaster.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.globant.agilepodmaster.organization.Organization;
import com.globant.agilepodmaster.snapshot.SnapshotEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Product of an organization. It is composed of one or more projects.
 * @author jose.dominguez@globant.com
 *
 */
@Entity
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Product extends SnapshotEntity {
  @NonNull
  @Getter
  private String name;
  
  @NonNull
  @ManyToOne
  @JsonIgnore
  private Organization organization;
  
  @Setter
  private String description;
}
