package com.globant.agilepodmaster.sync;

public class AbortSyncException extends RuntimeException {
  
  public AbortSyncException(String message) {
    super(message);
  }
}