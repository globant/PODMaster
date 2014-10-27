package com.globant.agilepodmaster.product;

import com.globant.agilepodmaster.pod.ReaderStep;

public interface ProductReader extends ReaderStep<ProductBuilder> {
  void readInto(ProductBuilder builder);
}
