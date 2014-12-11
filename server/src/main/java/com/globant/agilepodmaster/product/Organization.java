package com.globant.agilepodmaster.product;

import com.globant.agilepodmaster.snapshot.SnapshotEntity;

import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Entity Organization whose metrics we want to show.
 * @author jose.dominguez@globant.com
 *
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Organization extends SnapshotEntity {
  
  @NonNull
  @Getter
  private String name;

  @Setter
  private String description;

}
