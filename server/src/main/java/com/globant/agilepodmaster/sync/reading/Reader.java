package com.globant.agilepodmaster.sync.reading;

import com.globant.agilepodmaster.sync.SyncContext;

/**
 * Interface that every Reader should implement.
 * 
 * @author jose.dominguez@globant.com
 *
 * @param <T> Builder needed for the reader.
 */
public interface Reader<T> {
  
  /**
   * Reads data form the data source.
   * @param builder to store the data read.
   * @param context context to log the reading process.
   */
  public void readInto(T builder, SyncContext context);

}

