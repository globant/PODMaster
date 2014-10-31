package com.globant.agilepodmaster.sync.reading;

public interface ReleasesBuilder extends BuilderLog {

  ReleasesBuilder addRelease(String name);

  ReleasesBuilder addSprint(String name, String startDate, String endDate);

  ReleasesBuilder addSprintTask(String issueKey, String summary, String type,
      String status, String owner, String severity,
      String priority);

  ReleasesBuilder addBacklogTask(String issueKey, String summary, String type,
      String status, String owner, String severity,
      String priority);
  
  

}
