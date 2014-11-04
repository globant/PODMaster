package com.globant.agilepodmaster.sync.reading;

/**
 * Defines the functionality of a log for a builder.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public interface BuilderLog {

  /**
   * Logs a warn message.
   * @param message the message.
   */
  public void warnMessage(String message);
  
  /**
   * Logs an info message.
   * @param message the message.
   */
  public void infoMessage(String message);
  
  /**
   * Logs an error message.
   * @param message the message.
   */
  public void errorMessage(String message);

}
