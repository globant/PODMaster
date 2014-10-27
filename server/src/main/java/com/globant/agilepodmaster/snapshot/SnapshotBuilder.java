package com.globant.agilepodmaster.snapshot;

import com.globant.agilepodmaster.pod.PodBuilder;
import com.globant.agilepodmaster.product.ProductBuilder;
import com.globant.agilepodmaster.sync.Builder;

public interface SnapshotBuilder extends Builder<Snapshot>, PodBuilder, ProductBuilder {
}
