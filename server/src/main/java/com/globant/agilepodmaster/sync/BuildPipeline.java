package com.globant.agilepodmaster.sync;

import lombok.RequiredArgsConstructor;

import com.globant.agilepodmaster.pod.ReaderStep;

@RequiredArgsConstructor
public class BuildPipeline<T> {
  private final Builder<T> builder;
  
  public BuildPipeline<T> accept(ReaderStep step) {
    step.readInto(builder);
    return this;
  }
  
  public T execute() {
    return builder.build();
  }
}
