package com.globant.agilepodmaster.core;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Immutable;

import com.globant.agilepodmaster.metrics.partition.Partition;
import com.globant.agilepodmaster.metrics.partition.Partitioner;

@Entity
@Immutable
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectMetric extends MetricData {
  @Getter @Setter
  private int remainingStoryPoints;
  
  @Getter
  @NonNull
  @ManyToOne
  private Project project;
  
  public ProjectMetric(Project project, Pod pod) {
    super(pod);
    this.project = project;
  }

  @Override
  public Partition<?> visit(Partitioner<? extends MetricData, ? extends Partition<?>> p) {
    return p.extractPartition(this);
  }
}
