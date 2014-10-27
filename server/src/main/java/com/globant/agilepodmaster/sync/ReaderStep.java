package com.globant.agilepodmaster.sync;

public interface ReaderStep<B> {
  void readInto(B builder);
}
