package com.globant.agilepodmaster.sync;

/**
 * Runtime exception to report encryption issues.
 * @author jose.dominguez@globant.com
 *
 */
public class EncryptionException extends RuntimeException {
  
  /**
   * Constructor.
   * @param message exception message.
   * @param exception the exception.
   */
  public EncryptionException(String message, Exception exception) {
    super(message, exception);
  }
}
