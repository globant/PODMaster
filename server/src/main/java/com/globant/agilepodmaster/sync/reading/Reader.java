package com.globant.agilepodmaster.sync.reading;

import com.globant.agilepodmaster.sync.datamodel.ProjectDataSet;

public interface Reader {
  
  public void readInto(ProjectDataSet.Builder builder);

}
