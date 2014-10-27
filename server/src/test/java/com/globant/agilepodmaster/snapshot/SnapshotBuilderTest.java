package com.globant.agilepodmaster.snapshot;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.globant.agilepodmaster.AbstractUnitTest;
import com.globant.agilepodmaster.pod.PodReader;
import com.globant.agilepodmaster.product.ProductReader;
import com.globant.agilepodmaster.sync.BuildPipeline;

public class SnapshotBuilderTest extends AbstractUnitTest {

  @Autowired
  private SnapshotBuilder builder;

  @Autowired
  private PodReader podReader;

  @Autowired
  private ProductReader productReader;

  @Test
  @Ignore
  public void shouldCreateSnapshot() {
    Snapshot snapshot = 
        new BuildPipeline<Snapshot>(builder)
        .accept(podReader)
        .accept(productReader)
        .execute();
    
    System.out.println(snapshot);
  }
}
