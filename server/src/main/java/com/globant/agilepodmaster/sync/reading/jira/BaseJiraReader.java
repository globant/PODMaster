package com.globant.agilepodmaster.sync.reading.jira;

import com.globant.agilepodmaster.sync.SyncContext;
import com.globant.agilepodmaster.sync.reading.BaseReader;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import lombok.Getter;

/**
 * Base class for all Jira readers.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public abstract class BaseJiraReader extends BaseReader {
  
  @Getter
  protected JiraCustomSettings jiraCustomSettings;
  
  protected RestTemplate restClient;
  protected HttpEntity<String> request;
  protected String rootUrl;

  /**
   * BaseJiraReader constructor.
   * 
   * @param syncContext synchronization process context.
   * @param jiraCustomSettings Jira settings.
   */
  public BaseJiraReader(SyncContext syncContext,
      JiraCustomSettings jiraCustomSettings) {
    super(syncContext);

    this.jiraCustomSettings = jiraCustomSettings;

    AccessToken token = AccessToken.Decrypt(
        jiraCustomSettings.getAccessToken(), "jira access token");

    final String plainCreds = token.getUsername() + ":" + token.getPassword();
    final byte[] plainCredsBytes = plainCreds.getBytes();
    final byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    final String base64Creds = new String(base64CredsBytes);

    final HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(new MediaType("application",
        "json")));
    headers.add("Authorization", "Basic " + base64Creds);

    request = new HttpEntity<String>(headers);
    rootUrl = jiraCustomSettings.getJiraRoot();
    restClient = new RestTemplate();

  }
  
}
