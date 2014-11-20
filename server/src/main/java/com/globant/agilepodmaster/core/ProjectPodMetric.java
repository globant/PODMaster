package com.globant.agilepodmaster.core;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

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
public class ProjectPodMetric extends AbstractMetric {
  @Getter @Setter
  private int remainingStoryPoints;
  
  @Getter
  @NonNull
  @ManyToOne
  private Project project;

  @NonNull @Getter
  @ManyToOne @NotNull
  private Pod pod;
  
  public ProjectPodMetric(Project project, Pod pod) {
    this.project = project;
    this.pod = pod;
  }

  @Override
  public Partition<?> visit(Partitioner<? extends Partition<?>> p) {
    return p.extractPartition(this);
  }
}
