package com.globant.agilepodmaster.metrics;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import com.globant.agilepodmaster.core.AbstractEntity;
import com.globant.agilepodmaster.core.Pod;
import com.globant.agilepodmaster.core.Sprint;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public class SprintPodMetric extends AbstractEntity {
  @NonNull @Getter
  @ManyToOne @NotNull
  private Sprint sprint;

  @NonNull @Getter
  @ManyToOne @NotNull
  private Pod pod;
  
  @Getter @Setter
  private int acceptedStoryPoints;
  
  @Getter @Setter
  private int hoursInAcceptedStories;
  
  public SprintPodMetric(Sprint sprint, Pod pod) {
    this.sprint = sprint;
    this.pod = pod;
  }
}
