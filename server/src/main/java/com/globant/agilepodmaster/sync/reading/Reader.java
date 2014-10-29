package com.globant.agilepodmaster.sync.reading;

public interface Reader<T> {
  
  public void readInto(T builder);

}

