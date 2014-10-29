package com.globant.agilepodmaster.sync.reading;

public interface BuilderLog {

  public void warnMessage(String message);
  
  public void infoMessage(String message);
  
  public void errorMessage(String message);

}
