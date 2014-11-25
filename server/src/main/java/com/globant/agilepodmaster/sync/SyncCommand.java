package com.globant.agilepodmaster.sync;

import com.globant.agilepodmaster.core.Snapshot;
import com.globant.agilepodmaster.core.SnapshotRepository;
import com.globant.agilepodmaster.sync.reading.db.PodsReader;
import com.globant.agilepodmaster.sync.reading.jira.JiraAPIFactory;
import com.globant.agilepodmaster.sync.reading.jira.JiraRestClient;
import com.globant.agilepodmaster.sync.reading.jira.ReleasesReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * Command to process the synchronization.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Slf4j
@Component
public class SyncCommand {

  @Autowired
  ReleasesReader releasesReader;
  
  @Autowired
  RestTemplate restTemplate;

  @Autowired
  PodsReader podsReader;

  @Autowired
  SnapshotRepository snapshotRepository;

  @Autowired
  private Environment env;

  /**
   * Executes the synchronization.
   * 
   * @param organizationName organizationName whose synchronization we want.
   * @return the context that tells details of the process.
   */
  public SyncContext execute(String organizationName) {

    releasesReader.setConfiguration(loadConfiguration(organizationName));

    SyncContext context = new SyncContext();

    SnapshotBuilder snapshotBuilder = new SnapshotBuilder(context);
    podsReader.readInto(snapshotBuilder, context);
    releasesReader.readInto(snapshotBuilder, context);
    Snapshot snapshot = snapshotBuilder.build();

    snapshot = snapshotRepository.save(snapshot);
    
    context.info("Snapshot id: " + snapshot.getId());
    return context;

  }

  protected ReleaseReaderConfiguration loadConfiguration(String orgName) {

    List<ReleaseReaderConfiguration.Product> productConfigs = 
        new ArrayList<ReleaseReaderConfiguration.Product>();

    for (String productName : loadProductNames(orgName)) {

      List<ReleaseReaderConfiguration.Project> projectConfigs = 
          new ArrayList<ReleaseReaderConfiguration.Project>();

      for (String projectName : loadProjectNames(orgName, productName)) {
        String jiraProjectName = loadProjectProperty(orgName, productName,
            projectName, "jira.project_name");
        String jiraRapidViewId = loadProjectProperty(orgName, productName,
            projectName, "jira.rapid_view_id");
        String jiraUsername = loadProjectProperty(orgName, productName,
            projectName, "jira.username");
        String jiraPassword = loadProjectProperty(orgName, productName,
            projectName, "jira.password");
        String jiraUrlroot = loadProjectProperty(orgName, productName,
            projectName, "jira.urlroot");

        JiraRestClient jiraRestClient = new JiraAPIFactory()
            .withCredentials(jiraUsername, jiraPassword)
            .withTemplate(restTemplate).withUrlRoot(jiraUrlroot).create();

        projectConfigs.add(new ReleaseReaderConfiguration.Project(projectName,
            jiraProjectName, jiraRapidViewId, jiraRestClient));
      }
      ReleaseReaderConfiguration.Product productConfig = new ReleaseReaderConfiguration.Product(
          productName, projectConfigs);

      productConfigs.add(productConfig);

    }

    ReleaseReaderConfiguration configuration = new ReleaseReaderConfiguration(
        orgName, productConfigs);
    return configuration;

  }

  void checkProperty(String value, String property) {
    if (StringUtils.isEmpty(value)) {
      throw new PropertyNotFoundException("This property was not found: "
          + property);
    }
  }

  String[] loadProductNames(String orgName) {
    String property = orgName + ".products";
    String products = env.getProperty(property);
    checkProperty(products, property);
    return StringUtils.commaDelimitedListToStringArray(products);

  }

  String[] loadProjectNames(String orgName, String productName) {
    String property = orgName + "." + productName + ".projects";
    String projects = env.getProperty(property);
    checkProperty(projects, property);
    return StringUtils.commaDelimitedListToStringArray(projects);
  }

  private String loadProjectProperty(String orgName, String productName,
      String projectName, String propertyName) {
    String property = orgName + "." + productName + "." + projectName + "."
        + propertyName;
    String propertyValue = env.getProperty(property);
    checkProperty(propertyValue, property);
    return propertyValue;
  }

  /**
   * Exception for properties not found in application.properties.
   * @author jose.dominguez@globant.com
   *
   */
  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Application Property Not Found")
  public static class PropertyNotFoundException extends RuntimeException {
    /**
     * Constructor.
     * @param msg error message.
     */
    public PropertyNotFoundException(String msg) {
      super(msg);
      log.error(msg);
    }
  }
}
