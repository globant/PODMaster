package com.globant.agilepodmaster.sync;

/**
 * Runtime exception to report conexion issues with data sources.
 * @author jose.dominguez@globant.com
 *
 */
public class ConexionRestException extends RuntimeException {
  
  /**
   * Constructor.
   * @param message exception message.
   * @param exception the exception.
   */
  public ConexionRestException(String message, Exception exception) {
    super(message, exception);
  }
}
