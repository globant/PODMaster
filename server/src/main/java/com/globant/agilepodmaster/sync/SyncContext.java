package com.globant.agilepodmaster.sync;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * This class manages basically logging during synchronization process.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public class SyncContext {


  @Getter
  @Setter
  private int elementCount;

  @Getter
  private List<SyncLogEntry> logEntries;

  /**
   * Constructor.
   */
  public SyncContext() {
    logEntries = new ArrayList<SyncLogEntry>();
  }

  /**
   * Logs info message.
   * @param message the message.
   */
  public void info(String message) {
    logEntries.add(new SyncLogEntry(logEntries.size() + 1, "Info", message));
  }

  /**
   * Logs warn message.
   * @param message the message.
   */
  public void warn(String message) {
    logEntries.add(new SyncLogEntry(logEntries.size() + 1, "Warn", message));
  }

  /**
   * Logs error message.
   * @param message the message.
   */
  public void error(String message) {
    logEntries.add(new SyncLogEntry(logEntries.size() + 1, "Error", message));
  }


  /**
   * This class represents a log record of SyncContext.
   */
  @Data
  private static class SyncLogEntry {

    public int sequence;

    public Date createdDate;

    public String eventType;

    public String message;

    public SyncLogEntry(final int sequence, final String eventType,
        final String message) {
      createdDate = new Date();
      this.sequence = sequence;
      this.eventType = eventType;
      this.message = message;
    }

  }


}
