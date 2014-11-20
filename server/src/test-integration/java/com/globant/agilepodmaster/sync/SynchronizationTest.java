package com.globant.agilepodmaster.sync;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.globant.agilepodmaster.AbstractIntegrationTest;
import com.globant.agilepodmaster.core.OrganizationRepository;
import com.globant.agilepodmaster.core.Pod;
import com.globant.agilepodmaster.core.PodMember;
import com.globant.agilepodmaster.core.PodMemberRepository;
import com.globant.agilepodmaster.core.PodRepository;
import com.globant.agilepodmaster.core.ProductRepository;
import com.globant.agilepodmaster.core.ProjectRepository;
import com.globant.agilepodmaster.core.QRelease;
import com.globant.agilepodmaster.core.QSprintPodMetric;
import com.globant.agilepodmaster.core.Release;
import com.globant.agilepodmaster.core.ReleaseRepository;
import com.globant.agilepodmaster.core.Snapshot;
import com.globant.agilepodmaster.core.SnapshotRepository;
import com.globant.agilepodmaster.core.Sprint;
import com.globant.agilepodmaster.core.SprintPodMetricRepository;
import com.globant.agilepodmaster.core.SprintRepository;
import com.globant.agilepodmaster.core.Task;
import com.globant.agilepodmaster.core.TaskRepository;
import com.globant.agilepodmaster.sync.reading.db.PodsReader;
import com.globant.agilepodmaster.sync.reading.jira.JiraAPIFactory;
import com.globant.agilepodmaster.sync.reading.jira.JiraRestClient;
import com.globant.agilepodmaster.sync.reading.jira.ReleasesReader;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Integration test for Jira Readers.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public class SynchronizationTest extends AbstractIntegrationTest {

  @Autowired
  ReleasesReader releasesReader;

  @Autowired
  PodsReader podsReader;

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  SnapshotRepository snapshotRepository;
  
  @Autowired
  OrganizationRepository organizationRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  ReleaseRepository releaseRepository;
  
  @Autowired
  PodRepository podRepository;
  
  @Autowired
  PodMemberRepository podMemberRepository;

  @Autowired
  SprintRepository sprintRepository;
  
  @Autowired
  SprintPodMetricRepository sprintPodMetricRepository;

  @Autowired
  TaskRepository taskRepository;
  
  /**
   * Test for ProjectDataSetBuilder.
   */
  @Test @Ignore
  public void testSnapshotBuilder() {

    JiraRestClient jiraRestClient = new JiraAPIFactory()
        .withCredentials("jose.dominguez", "Jose1234").withTemplate(restTemplate)
        .withUrlRoot("http://nglb008dxu00.tx.corp.globant.com:8080/").create();

    ReleaseReaderConfiguration.Project project = new ReleaseReaderConfiguration.Project(
        "TeammateIntelligence", "Teammate Intelligence", "3", jiraRestClient);
    ReleaseReaderConfiguration.Product product = new ReleaseReaderConfiguration.Product(
        "FIFA14", Arrays.asList(project));
    ReleaseReaderConfiguration configuration = new ReleaseReaderConfiguration(
        "EA", Arrays.asList(product));

    releasesReader.setConfiguration(configuration);

    JiraRestClient jiraRestClient = new JiraAPIFactory()
        .withCredentials(username, password).withTemplate(restTemplate)
        .withUrlRoot(urlRoot).create();

    SnapshotBuilder snapshotBuilder = new SnapshotBuilder(context);
    podsReader.readInto(snapshotBuilder, context);
    releasesReader.readInto(snapshotBuilder, context);
    Snapshot snapshot = snapshotBuilder.build();

    snapshotRepository.save(snapshot);

    BooleanExpression releaseSnapshotQuery = QRelease.release.snapshot().eq(snapshot);
    
    Release release = releaseRepository.findOne(releaseSnapshotQuery);
    assertThat(release, notNullValue());

    List<Sprint> sprints = sprintRepository.findByRelease(release);
    assertThat(sprints, hasSize(greaterThan(3)));

    List<Task> taskssprint1 = taskRepository.findByReleaseAndSprint(
        release, sprints.get(0));
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

    BooleanExpression sprintSpmQuery = QSprintPodMetric.sprintPodMetric.sprint()
        .eq(sprint1);
    BooleanExpression podSpmQuery = QSprintPodMetric.sprintPodMetric.pod()
        .eq(pod1);
    assertThat(
        sprintPodMetricRepository.findOne(podSpmQuery.and(sprintSpmQuery))
            .getAcceptedStoryPoints(), equalTo(60));

    sprintSpmQuery = QSprintPodMetric.sprintPodMetric.sprint().eq(sprint1);
    podSpmQuery = QSprintPodMetric.sprintPodMetric.pod().eq(pod2);
    assertThat(
        sprintPodMetricRepository.findOne(podSpmQuery.and(sprintSpmQuery))
            .getAcceptedStoryPoints(), equalTo(90));

    sprintSpmQuery = QSprintPodMetric.sprintPodMetric.sprint().eq(sprint2);
    podSpmQuery = QSprintPodMetric.sprintPodMetric.pod().eq(pod1);
    assertThat(
        sprintPodMetricRepository.findOne(podSpmQuery.and(sprintSpmQuery))
            .getAcceptedStoryPoints(), equalTo(60));

    sprintSpmQuery = QSprintPodMetric.sprintPodMetric.sprint().eq(sprint2);
    podSpmQuery = QSprintPodMetric.sprintPodMetric.pod().eq(pod2);
    assertThat(
        sprintPodMetricRepository.findOne(podSpmQuery.and(sprintSpmQuery))
            .getAcceptedStoryPoints(), equalTo(90));

    sprintSpmQuery = QSprintPodMetric.sprintPodMetric.sprint().eq(sprint3);
    podSpmQuery = QSprintPodMetric.sprintPodMetric.pod().eq(pod1);
    assertThat(
        sprintPodMetricRepository.findOne(podSpmQuery.and(sprintSpmQuery))
            .getAcceptedStoryPoints(), equalTo(70));

    sprintSpmQuery = QSprintPodMetric.sprintPodMetric.sprint().eq(sprint3);
    podSpmQuery = QSprintPodMetric.sprintPodMetric.pod().eq(pod2);
    assertThat(
        sprintPodMetricRepository.findOne(podSpmQuery.and(sprintSpmQuery))
            .getAcceptedStoryPoints(), equalTo(80));

    sprintSpmQuery = QSprintPodMetric.sprintPodMetric.sprint().eq(sprint4);
    podSpmQuery = QSprintPodMetric.sprintPodMetric.pod().eq(pod1);
    assertThat(
        sprintPodMetricRepository.findOne(podSpmQuery.and(sprintSpmQuery))
            .getAcceptedStoryPoints(), equalTo(80));

    sprintSpmQuery = QSprintPodMetric.sprintPodMetric.sprint().eq(sprint4);
    podSpmQuery = QSprintPodMetric.sprintPodMetric.pod().eq(pod2);
    assertThat(
        sprintPodMetricRepository.findOne(podSpmQuery.and(sprintSpmQuery))
            .getAcceptedStoryPoints(), equalTo(70));

  }
}

