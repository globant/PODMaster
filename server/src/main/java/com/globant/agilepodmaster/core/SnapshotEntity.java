package com.globant.agilepodmaster.core;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter(AccessLevel.PACKAGE)
public class SnapshotEntity extends AbstractEntity {
  @ManyToOne
  private Snapshot snapshot;
}
