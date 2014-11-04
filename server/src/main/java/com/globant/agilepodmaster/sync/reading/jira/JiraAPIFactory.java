package com.globant.agilepodmaster.sync.reading.jira;

import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

public class JiraAPIFactory {
  private String username;
  private String password;
  private String urlRoot;
  private RestTemplate restTemplate = new RestTemplate();

  public JiraRestClient create() {
    Assert.notNull(username, "username must not be null");
    Assert.notNull(password, "password must not be null");
    Assert.notNull(urlRoot, "jiraRoot must not be null");
    Assert.notNull(restTemplate, "restTemplate must not be null");
    
    return new JiraRestClient(username, password, urlRoot, restTemplate);
  }
  
  public JiraAPIFactory withCredentials(String username, String password) {
    this.username = username;
    this.password = password;
    
    return this;
  }
  
  public JiraAPIFactory withUrlRoot(String root) {
    this.urlRoot = root;
    return this;
  }

  public JiraAPIFactory withTemplate(RestTemplate template) {
    this.restTemplate = template;
    return this;
  }
}
