package com.globant.agilepodmaster.pod;

import com.globant.agilepodmaster.snapshot.SnapshotEntity;

import javax.persistence.Entity;

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
public class Pod extends SnapshotEntity {
  
  @Getter
  @NonNull
  private String name;
  
//  @NonNull
//  @ManyToOne
//  @JsonIgnore
//  @Getter
//  private Sprint sprint;
}