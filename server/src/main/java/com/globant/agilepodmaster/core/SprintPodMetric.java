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
public class SprintPodMetric extends AbstractMetric {
  @NonNull @Getter
  @ManyToOne @NotNull
  private Sprint sprint;

  @Getter @Setter
  private int acceptedStoryPoints;

  @Getter @Setter
  private int plannedStoryPoints;
  
  @Getter @Setter
  private int accumutaledStoryPoints;


  @Getter @Setter
  private int numberOfBugs;

  @NonNull @Getter
  @ManyToOne @NotNull
  private Pod pod;

  public SprintPodMetric(Sprint sprint, Pod pod) {
    this.sprint = sprint;
    this.pod = pod;
  }

  public double getEstimationAccuracy() {
    return plannedStoryPoints / (double) acceptedStoryPoints;
  }
  
  @Override
  public Partition<?> visit(Partitioner<? extends Partition<?>> partitioner) {
    return partitioner.extractPartition(this);
  }

  public Project getProject() {
    return sprint.getRelease().getProject();
  }
}
