package com.globant.agilepodmaster.sync;

/**
 * Runtime exception to report conexion response errors.
 * @author jose.dominguez@globant.com
 *
 */
public class ConexionResponseErrorException extends RuntimeException {
  
  /**
   * Constructor.
   * @param message exception message.
   */
  public ConexionResponseErrorException(String message) {
    super(message);
  }
}
