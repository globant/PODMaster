package com.globant.agilepodmaster.metrics.partition;

import lombok.EqualsAndHashCode;

import com.globant.agilepodmaster.core.ProjectPodMetric;
import com.globant.agilepodmaster.core.SprintPodMetric;

/**
 * Abstract class to extract partitions from a Metric. Each Partition (Pod, Project, Quarter,
 * Sprint) has a Partitioner.
 * 
 * @author Andres Postiglioni.
 *
 * @param <P> the partition asociated to this partitioner.
 */
@EqualsAndHashCode
public abstract class Partitioner<P> {
  private final String acceptedDimension;
  
  /**
   * Constructor.
   * @param acceptedDimension the name of the partition.
   */
  public Partitioner(String acceptedDimension) {
    if (acceptedDimension == null) {
      throw new IllegalArgumentException("dimension must not be null");
    }

    this.acceptedDimension = acceptedDimension;
  }

  /**
   * Tells if this partitioner accepts a partition.
   * @param dimension the name of the partition.
   * @return true if accepts, false otherwise.
   */
  public boolean accepts(String dimension) {
    return acceptedDimension.equalsIgnoreCase(dimension);
  }
  
  @Override
  public String toString() {
    return this.acceptedDimension;
  }

  /**
   * Extract a partition form a SprintPod metric.
   * @param sprintPodMetric the SprintPod metric.
   * @return the partition.
   */
  public abstract P extractPartition(SprintPodMetric sprintPodMetric);
  
  /**
   * Extract a partition form a ProjectPod metric.
   * @param projectPodMetric the ProjectPod metric.
   * @return the partition.
   */
  public abstract P extractPartition(ProjectPodMetric projectPodMetric);
}
