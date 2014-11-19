package com.globant.agilepodmaster.core;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.globant.agilepodmaster.metrics.partition.Partition;
import com.globant.agilepodmaster.metrics.partition.Partitioner;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@ToString
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class MetricData extends SnapshotEntity {
  @NonNull @Getter
  @ManyToOne @NotNull
  private Pod pod;

  public MetricData(Pod pod) {
    this.pod = pod;
  }

  public abstract Partition<?> visit(Partitioner<? extends MetricData, ? extends Partition<?>> p);
}
