package com.globant.agilepodmaster.snapshot;

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
import com.globant.agilepodmaster.pod.Pod;
import com.globant.agilepodmaster.project.Project;
import com.globant.agilepodmaster.sprint.Sprint;

/**
 * Entity that keeps metrics of a Sprint and a Pod.
 * @author jose.dominguez@globant.com
 *
 */
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

  /**
   * Constructor.
   * @param sprint the sprint.
   * @param pod the pod.
   */
  public SprintPodMetric(Sprint sprint, Pod pod) {
    this.sprint = sprint;
    this.pod = pod;
  }

  /**
   * Calculates and return the accuracy.
   * @return the accuracy.
   */
  public double getEstimationAccuracy() {
    return plannedStoryPoints / (double) acceptedStoryPoints;
  }
  
  @Override
  public Partition<?> visit(Partitioner<? extends Partition<?>> partitioner) {
    return partitioner.extractPartition(this);
  }

  /**
   * Return the project associated to this object..
   * @return the project.
   */
  public Project getProject() {
    return sprint.getRelease().getProject();
  }
}
