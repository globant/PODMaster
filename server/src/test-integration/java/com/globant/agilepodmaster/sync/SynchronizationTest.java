package com.globant.agilepodmaster.sync;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.globant.agilepodmaster.AbstractIntegrationTest;
import com.globant.agilepodmaster.core.Organization;
import com.globant.agilepodmaster.core.OrganizationRepository;
import com.globant.agilepodmaster.core.Pod;
import com.globant.agilepodmaster.core.PodMember;
import com.globant.agilepodmaster.core.PodMemberRepository;
import com.globant.agilepodmaster.core.PodRepository;
import com.globant.agilepodmaster.core.Product;
import com.globant.agilepodmaster.core.ProductRepository;
import com.globant.agilepodmaster.core.Project;
import com.globant.agilepodmaster.core.ProjectRepository;
import com.globant.agilepodmaster.core.QSprintPodMetric;
import com.globant.agilepodmaster.core.Release;
import com.globant.agilepodmaster.core.ReleaseRepository;
import com.globant.agilepodmaster.core.Snapshot;
import com.globant.agilepodmaster.core.SnapshotRepository;
import com.globant.agilepodmaster.core.Sprint;
import com.globant.agilepodmaster.core.SprintPodMetric;
import com.globant.agilepodmaster.core.SprintPodMetricRepository;
import com.globant.agilepodmaster.core.SprintRepository;
import com.globant.agilepodmaster.core.Task;
import com.globant.agilepodmaster.core.TaskRepository;
import com.globant.agilepodmaster.sync.reading.db.PodsReader;
import com.globant.agilepodmaster.sync.reading.jira.JiraAPIFactory;
import com.globant.agilepodmaster.sync.reading.jira.JiraCustomSettings;
import com.globant.agilepodmaster.sync.reading.jira.JiraRestClient;
import com.globant.agilepodmaster.sync.reading.jira.ReleasesReader;
import com.mysema.query.types.expr.BooleanExpression;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  PodRepository podRepository;

  @Autowired
  PodMemberRepository podMemberRepository;

  @Autowired
  SprintPodMetricRepository sprintPodMetricRepository;

  @Autowired
  SnapshotRepository snapshotRepository;


  /**
   * Test for ProjectDataSetBuilder.
   */
  @Ignore
  @Test
  public void testSnapshotBuilderInOurProjectJra() {

    Organization organization = new Organization("Org Prueba");
    Product product = new Product("Prod Prueba", organization);
    Project project = new Project("Proj Prueba", product);

    organizationRepository.save(organization);
    product = productRepository.save(product);
    project = projectRepository.save(project);

    String username = "jose.dominguez";
    String password = "XXX";
    String urlRoot = "https://jira.corp.globant.com/";

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


  /**
   * Test for ProjectDataSetBuilder.
   */
  @Ignore
  @Test
  public void testSnapshotBuilderInTestJira() {

    Iterable<Organization> ito = organizationRepository.findAll();
    Organization organization = ito.iterator().next();
    assertThat(organization.getName(), equalTo("EA"));

    Iterable<Product> itp = productRepository.findAll();
    Product product = itp.iterator().next();
    assertThat(product.getName(), equalTo("FIFA14"));

    Iterable<Project> itpr = projectRepository.findAll();
    Project project = itpr.iterator().next();
    assertThat(project.getName(), equalTo("TeammateIntelligence"));

    String username = "jose.dominguez";
    String password = "Jose1234";
    String urlRoot = "http://nglb008dxu00.tx.corp.globant.com:8080/";

    JiraRestClient jiraRestClient = new JiraAPIFactory()
        .withCredentials(username, password).withTemplate(restTemplate)
        .withUrlRoot(urlRoot).create();

    JiraCustomSettings settings = new JiraCustomSettings();
    settings.setJiraProjectName("Teammate Intelligence");
    settings.setJiraRapidViewId("3");
    settings.projectId = project.getId();

    releasesReader.setRestJiraClient(jiraRestClient);
    List<JiraCustomSettings> settingsList = new ArrayList<JiraCustomSettings>();
    settingsList.add(settings);
    releasesReader.setSettingsList(settingsList);

    Snapshot snapshot = snapshotBuilder.withProduct(product)
        .addReader(podsReader).addReader(releasesReader).build();

    snapshot = snapshotRepository.save(snapshot);

    assertThat(snapshot.getProduct(), equalTo(product));

    List<Release> releases = releaseRepository.findBySnapshot(snapshot);
    assertThat(releases, hasSize(1));

    List<Sprint> sprints = sprintRepository.findByRelease(releases.get(0));
    assertThat(sprints, hasSize(greaterThan(3)));

    List<Task> taskssprint1 = taskRepository.findByReleaseAndSprint(
        releases.get(0), sprints.get(0));
    assertThat(taskssprint1, hasSize(greaterThan(5)));

    Pod pod1 = podRepository.findByName("POD1").iterator().next();
    assertThat(pod1, notNullValue());

    List<PodMember> podMembers = podMemberRepository.findByPod(pod1);
    assertThat(podMembers, hasSize(3));

    Pod pod2 = podRepository.findByName("POD2").iterator().next();
    assertThat(pod2, notNullValue());

    podMembers = podMemberRepository.findByPod(pod2);
    assertThat(podMembers, hasSize(3));

    Sprint sprint1 = sprints.stream()
        .filter(s -> "Sprint 1".equals(s.getName())).findAny().get();
    Sprint sprint2 = sprints.stream()
        .filter(s -> "Sprint 2".equals(s.getName())).findAny().get();
    Sprint sprint3 = sprints.stream()
        .filter(s -> "Sprint 3".equals(s.getName())).findAny().get();
    Sprint sprint4 = sprints.stream()
        .filter(s -> "Sprint 4".equals(s.getName())).findAny().get();

    BooleanExpression sprintSpmQuery = QSprintPodMetric.sprintPodMetric.sprint
        .eq(sprint1);
    BooleanExpression podSpmQuery = QSprintPodMetric.sprintPodMetric.pod
        .eq(pod1);
    assertThat(
        sprintPodMetricRepository.findOne(podSpmQuery.and(sprintSpmQuery))
            .getAcceptedStoryPoints(), equalTo(60));

    sprintSpmQuery = QSprintPodMetric.sprintPodMetric.sprint.eq(sprint1);
    podSpmQuery = QSprintPodMetric.sprintPodMetric.pod.eq(pod2);
    assertThat(
        sprintPodMetricRepository.findOne(podSpmQuery.and(sprintSpmQuery))
            .getAcceptedStoryPoints(), equalTo(90));

    sprintSpmQuery = QSprintPodMetric.sprintPodMetric.sprint.eq(sprint2);
    podSpmQuery = QSprintPodMetric.sprintPodMetric.pod.eq(pod1);
    assertThat(
        sprintPodMetricRepository.findOne(podSpmQuery.and(sprintSpmQuery))
            .getAcceptedStoryPoints(), equalTo(60));

    sprintSpmQuery = QSprintPodMetric.sprintPodMetric.sprint.eq(sprint2);
    podSpmQuery = QSprintPodMetric.sprintPodMetric.pod.eq(pod2);
    assertThat(
        sprintPodMetricRepository.findOne(podSpmQuery.and(sprintSpmQuery))
            .getAcceptedStoryPoints(), equalTo(90));

    sprintSpmQuery = QSprintPodMetric.sprintPodMetric.sprint.eq(sprint3);
    podSpmQuery = QSprintPodMetric.sprintPodMetric.pod.eq(pod1);
    assertThat(
        sprintPodMetricRepository.findOne(podSpmQuery.and(sprintSpmQuery))
            .getAcceptedStoryPoints(), equalTo(70));

    sprintSpmQuery = QSprintPodMetric.sprintPodMetric.sprint.eq(sprint3);
    podSpmQuery = QSprintPodMetric.sprintPodMetric.pod.eq(pod2);
    assertThat(
        sprintPodMetricRepository.findOne(podSpmQuery.and(sprintSpmQuery))
            .getAcceptedStoryPoints(), equalTo(80));

    sprintSpmQuery = QSprintPodMetric.sprintPodMetric.sprint.eq(sprint4);
    podSpmQuery = QSprintPodMetric.sprintPodMetric.pod.eq(pod1);
    assertThat(
        sprintPodMetricRepository.findOne(podSpmQuery.and(sprintSpmQuery))
            .getAcceptedStoryPoints(), equalTo(80));

    sprintSpmQuery = QSprintPodMetric.sprintPodMetric.sprint.eq(sprint4);
    podSpmQuery = QSprintPodMetric.sprintPodMetric.pod.eq(pod2);
    assertThat(
        sprintPodMetricRepository.findOne(podSpmQuery.and(sprintSpmQuery))
            .getAcceptedStoryPoints(), equalTo(70));


  }

}
