package com.globant.agilepodmaster.sync.reading.jira;

import com.globant.agilepodmaster.sync.SyncContext;
import com.globant.agilepodmaster.sync.datamodel.ProjectDataSet;
import com.globant.agilepodmaster.sync.datamodel.ReleaseData;
import com.globant.agilepodmaster.sync.datamodel.TaskData;

import java.util.List;

public class BacklogReader extends BaseTaskReader {

  public BacklogReader(SyncContext syncContext,
      JiraCustomSettings jiraCustomSettings) {
    super(syncContext, jiraCustomSettings);
  }

  @Override
  protected String getName() {
    return  "Jira Backlog Reader"; 
  }

  @Override
  public void read(ProjectDataSet projectDataSet) {

    ReleaseData release = getOrCreateReleaseFor(projectDataSet, null);
    List<TaskData> tasks = getTaskTree(projectDataSet,
        "sprint=null AND project=\"" + jiraCustomSettings.jiraProjectName
            + "\"");
    release.getBacklog().addAll(tasks);
  }
  

}
