package com.globant.agilepodmaster.sync;

/**
 * Runtime exception to report a synchonisation issue.
 * @author jose.dominguez@globant.com
 *
 */
public class AbortSyncException extends RuntimeException {
  
  /**
   * Constructor.
   * @param message the exception message.
   */
  public AbortSyncException(String message) {
    super(message);
  }
}