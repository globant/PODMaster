package com.globant.agilepodmaster.sync;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * This class manages basically logging during synchronization process.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Slf4j
public class SyncContext {

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
    log.info(message);
    logEntries.add(new SyncLogEntry(logEntries.size() + 1, "Info", message));
  }

  /**
   * Logs warn message.
   * @param message the message.
   */
  public void warn(String message) {
    log.warn(message);
    logEntries.add(new SyncLogEntry(logEntries.size() + 1, "Warn", message));
  }

  /**
   * Logs error message.
   * @param message the message.
   */
  public void error(String message) {
    log.error(message);
    logEntries.add(new SyncLogEntry(logEntries.size() + 1, "Error", message));
  }


  /**
   * This class represents a log record of SyncContext.
   */
  @Data
  public static class SyncLogEntry {

    public int sequence;

    @Setter(AccessLevel.NONE)
    public Date createdDate;

    public String eventType;

    public String message;

    /**
     * Constructor.
     * @param sequence number of order.
     * @param eventType type of event.
     * @param message the message.
     */
    public SyncLogEntry(final int sequence, final String eventType,
        final String message) {
      createdDate = new Date();
      this.sequence = sequence;
      this.eventType = eventType;
      this.message = message;
    }

    /**
     * Getter of CreatedDate.
     * @return the date.
     */
    public Date getCreatedDate() {
      return createdDate != null
          ? (Date) createdDate.clone()
          : null;

    }

  }


}
