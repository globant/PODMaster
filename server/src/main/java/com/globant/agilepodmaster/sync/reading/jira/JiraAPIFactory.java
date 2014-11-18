package com.globant.agilepodmaster.sync.reading.jira;

import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * Factory Builder to create JiraRestClient with logging data and root url.
 * 
 * @author Andres Postiglioni
 *
 */
public class JiraAPIFactory {
  private String username;
  private String password;
  private String urlRoot;
  private RestTemplate restTemplate = new RestTemplate();

  /**
   * Creates object with that data collected.
   * @return a JiraRestClient
   */
  public JiraRestClient create() {
    Assert.notNull(username, "username must not be null");
    Assert.notNull(password, "password must not be null");
    Assert.notNull(urlRoot, "jiraRoot must not be null");
    Assert.notNull(restTemplate, "restTemplate must not be null");
    
    return new JiraRestClient(username, password, urlRoot, restTemplate);
  }
  
  /**
   * Adds credencials.
   * @param username the Jira user name.
   * @param password the Jira password.
   * @return the same JiraRestClient instance.
   */
  public JiraAPIFactory withCredentials(String username, String password) {
    this.username = username;
    this.password = password;
    
    return this;
  }
  
  /**
   * Adds root url of Jira .
   * @param root the root url.
   * @return the same JiraRestClient instance.
   */
  public JiraAPIFactory withUrlRoot(String root) {
    this.urlRoot = root;
    return this;
  }

  /**
   * Add rest template. 
   * @param template rest template.
   * @return the same JiraRestClient instance.
   */
  public JiraAPIFactory withTemplate(RestTemplate template) {
    this.restTemplate = template;
    return this;
  }
}
