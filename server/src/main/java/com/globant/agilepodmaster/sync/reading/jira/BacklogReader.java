package com.globant.agilepodmaster.sync.reading.jira;

import com.globant.agilepodmaster.sync.datamodel.ProjectDataSet;
import com.globant.agilepodmaster.sync.datamodel.ReleaseData;
import com.globant.agilepodmaster.sync.datamodel.TaskData;

import java.util.List;

public class BacklogReader extends BaseTaskReader {

  public BacklogReader(JiraCustomSettings jiraCustomSettings) {
    super(jiraCustomSettings);
  }

  public void readInto(ProjectDataSet.Builder builder) {

    ReleaseData release = builder.getOrCreateReleaseFor(null);
    List<TaskData> tasks = getTaskTree(builder,
        "sprint=null AND project=\"" + jiraCustomSettings.jiraProjectName
            + "\"");
    release.getBacklog().addAll(tasks);
  }
  

}
