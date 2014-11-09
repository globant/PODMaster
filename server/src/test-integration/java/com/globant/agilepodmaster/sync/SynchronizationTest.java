package com.globant.agilepodmaster.sync;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import com.globant.agilepodmaster.AbstractIntegrationTest;
import com.globant.agilepodmaster.core.Organization;
import com.globant.agilepodmaster.core.OrganizationRepository;
import com.globant.agilepodmaster.core.Product;
import com.globant.agilepodmaster.core.ProductRepository;
import com.globant.agilepodmaster.core.Project;
import com.globant.agilepodmaster.core.ProjectRepository;
import com.globant.agilepodmaster.core.Release;
import com.globant.agilepodmaster.core.ReleaseRepository;
import com.globant.agilepodmaster.core.Snapshot;
import com.globant.agilepodmaster.core.Sprint;
import com.globant.agilepodmaster.core.SprintRepository;
import com.globant.agilepodmaster.core.Task;
import com.globant.agilepodmaster.core.TaskRepository;
import com.globant.agilepodmaster.sync.reading.db.PodsReader;
import com.globant.agilepodmaster.sync.reading.jira.JiraAPIFactory;
import com.globant.agilepodmaster.sync.reading.jira.JiraCustomSettings;
import com.globant.agilepodmaster.sync.reading.jira.JiraRestClient;
import com.globant.agilepodmaster.sync.reading.jira.ReleasesReader;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Integration test for Jira Readers.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public class SynchronizationTest extends AbstractIntegrationTest {

  @Autowired
  SnapshotBuilder snapshotBuilder;

  @Autowired
  ReleasesReader releasesReader;

  @Autowired
  PodsReader podsReader;

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  OrganizationRepository organizationRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  ReleaseRepository releaseRepository;

  @Autowired
  SprintRepository sprintRepository;

  @Autowired
  TaskRepository taskRepository;

  @Value("${podmaster.test.jira.username:jose.dominguez}")
  private String username;

  @Value("${podmaster.test.jira.password:XXXX}")
  private String password;

  @Value("${podmaster.test.jira.urlroot:https://jira.corp.globant.com/}")
  private String urlRoot;

  /**
   * Test for ProjectDataSetBuilder.
   */
  //@Test
  public void testSnapshotBuilder() {


    Organization organization = new Organization("Org Prueba");
    Product product = new Product("Prod Prueba", organization);
    Project project = new Project("Proj Prueba", product);

    organization = organizationRepository.save(organization);
    product = productRepository.save(product);
    project = projectRepository.save(project);

    JiraRestClient jiraRestClient = new JiraAPIFactory()
        .withCredentials(username, password).withTemplate(restTemplate)
        .withUrlRoot(urlRoot).create();

    JiraCustomSettings settings = new JiraCustomSettings();
    settings.setJiraProjectName("Pod Master");
    settings.setJiraRapidViewId("362");
    settings.projectId = project.getId();

    releasesReader.setRestJiraClient(jiraRestClient);
    List<JiraCustomSettings> settingsList = new ArrayList<JiraCustomSettings>();
    settingsList.add(settings);
    releasesReader.setSettingsList(settingsList);

    Snapshot snapshot = snapshotBuilder.withProduct(product)
        .addReader(podsReader).addReader(releasesReader).build();

    assertThat(snapshot.getProduct(), equalTo(product));

    List<Release> releases = releaseRepository.findBySnapshot(snapshot);
    assertThat(releases, hasSize(1));

    List<Sprint> sprints = sprintRepository.findByRelease(releases.get(0));
    assertThat(sprints, hasSize(greaterThan(3)));

    List<Task> taskssprint1 = taskRepository.findByReleaseAndSprint(
        releases.get(0), sprints.get(0));
    assertThat(taskssprint1, hasSize(greaterThan(10)));



  }
}
