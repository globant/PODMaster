package com.globant.agilepodmaster.sync;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * This class manages basically logging during synchonization process. 
 * @author jose.dominguez@globant.com
 *
 */
public class SyncContext {

  @Getter
  private int projectId;

  @Getter
  private boolean testRun;

  @Getter
  @Setter
  public int elementCount;

  @Getter
  public List<SyncLogEntry> logEntries;

  public SyncContext(final int projectId, final boolean testRun) {
    this.projectId = projectId;
    this.testRun = testRun;
    logEntries = new ArrayList<SyncLogEntry>();
  }

  private void log(String format, String eventType, Object... arguments) {
    logEntries.add(new SyncLogEntry(logEntries.size() + 1, eventType, String
        .format(format, arguments)));
  }

  public void info(String format, Object... arguments) {
    log(format, "Info", arguments);
  }

  public void warn(String format, Object... arguments) {
    log(format, "Warn", arguments);
  }

  public void error(String format, Object... arguments) {
    log(format, "Error", arguments);
  }


  /**
   * This class represents a log record of SyncContext.
   * 
   * @author jose.dominguez@globant.com
   *
   */
  class SyncLogEntry {

    @Getter
    @Setter
    public int sequence;

    @Getter
    @Setter
    public Date createdDate;

    @Getter
    @Setter
    public String eventType;

    @Getter
    @Setter
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
