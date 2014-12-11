package com.globant.agilepodmaster.snapshot;

import com.globant.agilepodmaster.AbstractEntity;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an entity that belongs to a snapshot. 
 * @author Andres Postiglioni.
 *
 */
@MappedSuperclass
@Getter
@Setter
public abstract class SnapshotEntity extends AbstractEntity {
  @ManyToOne
  private Snapshot snapshot;
}
