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

@Entity
@Immutable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@ToString
public class SprintPodMetric extends SnapshotEntity {
  @NonNull @Getter
  @ManyToOne @NotNull
  private Sprint sprint;

  @NonNull @Getter
  @ManyToOne @NotNull
  private Pod pod;
  
  @Getter @Setter
  private int acceptedStoryPoints;
  
  @Getter @Setter
  private int plannedStoryPoints;
  
  public SprintPodMetric(Sprint sprint, Pod pod) {
    this.sprint = sprint;
    this.pod = pod;
  }
}
