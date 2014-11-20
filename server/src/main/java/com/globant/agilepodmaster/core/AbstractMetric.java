package com.globant.agilepodmaster.core;

import javax.persistence.MappedSuperclass;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.globant.agilepodmaster.metrics.partition.Partition;
import com.globant.agilepodmaster.metrics.partition.Partitioner;

@ToString
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractMetric extends SnapshotEntity {
  public abstract Partition<?> visit(Partitioner<? extends Partition<?>> p);
}
