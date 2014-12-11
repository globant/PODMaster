package com.globant.agilepodmaster.snapshot;

import com.globant.agilepodmaster.metrics.partition.Partition;
import com.globant.agilepodmaster.metrics.partition.Partitioner;
import com.globant.agilepodmaster.project.Project;

import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity class that represents metrics of projects.
 * @author Jose Dominguez.
 *
 */
@Entity
@Immutable
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectMetric extends AbstractMetric {
  
  @Getter @Setter
  private int remainingStoryPoints;
  
  @Getter @Setter
  private double actualPercentComplete;
  
  @Getter
  @NonNull
  @ManyToOne
  private Project project;
  
  /**
   * Constructor.
   * @param project the project.
   */
  public ProjectMetric(Project project) {
    this.project = project;
  }

  @Override
  public Partition<?> visit(Partitioner<? extends Partition<?>> partitioner) {
    return partitioner.extractPartition(this);
  }
}