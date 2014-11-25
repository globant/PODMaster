package com.globant.agilepodmaster.core;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.globant.agilepodmaster.metrics.partition.Partition;
import com.globant.agilepodmaster.metrics.partition.Partitioner;

/**
 * Abstract class parent of all metric classes.
 * @author Andres Postiglioni
 *
 */
@ToString
//@MappedSuperclass
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractMetric extends SnapshotEntity {
  public abstract Partition<?> visit(Partitioner<? extends Partition<?>> partitioner);
}
