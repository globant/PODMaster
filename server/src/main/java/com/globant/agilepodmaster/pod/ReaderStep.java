package com.globant.agilepodmaster.pod;

public interface ReaderStep<B> {
  void readInto(B builder);
}
