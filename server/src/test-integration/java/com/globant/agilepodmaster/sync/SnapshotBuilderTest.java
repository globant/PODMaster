package com.globant.agilepodmaster.sync;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.globant.agilepodmaster.AbstractIntegrationTest;
import com.globant.agilepodmaster.core.OrganizationRepository;
import com.globant.agilepodmaster.core.Pod;
import com.globant.agilepodmaster.core.PodMember;
import com.globant.agilepodmaster.core.PodMemberRepository;
import com.globant.agilepodmaster.core.PodRepository;
import com.globant.agilepodmaster.core.ProductRepository;
import com.globant.agilepodmaster.core.ProjectMetric;
import com.globant.agilepodmaster.core.ProjectMetricRepository;
import com.globant.agilepodmaster.core.ProjectRepository;
import com.globant.agilepodmaster.core.QProjectMetric;
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
import com.mysema.query.types.expr.BooleanExpression;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;


/**
 * Testing Snapshot building.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Transactional
public class SnapshotBuilderTest extends AbstractIntegrationTest {
  @Autowired
  OrganizationRepository organizationRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  SnapshotRepository snapshotRepository;

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
  ProjectMetricRepository projectMetricRepository;

  /**
   * Test what was store in the DB.
   */
  @Test
  @SuppressWarnings("PMD")
  public void testBuild() {
    SyncContext context = new SyncContext();

    SnapshotBuilder snapshotBuilder = new SnapshotBuilder(context);

    snapshotBuilder
    .withPod("POD1")
      .withPodMember("Jose", "Dominguez", "jose@gmail.com").addToPod()
      .withPodMember("Maria", "Gomez", "maria@gmail.com").addToPod()
      .addToSnapshot()
    .withPod("POD2")
      .withPodMember("Ruben", "Dartes", "ruben@gmail.com").addToPod()
      .withPodMember("Juana", "Manso", "juana@gmail.com").addToPod()
      .addToSnapshot()
    .withOrganization("Org Prueba")
      .addProduct("Prod prueba")
       .addProject("Proj prueba", 100)
        .withRelease("release1")
        .withSprint("sprint1", new Date(), new Date())
        .withTask()
          .effort(10)
          .name("task1")
          .owner("jose@gmail.com")
          .status("Closed")
        .addToSprint()
        .withTask()
          .effort(10)
          .name("task2")
          .owner("maria@gmail.com")
          .status("Closed")
        .addToSprint()
        .withTask()
          .effort(10)
          .name("task3")
          .owner("maria@gmail.com")
        .addToSprint()
      .addToRelease()
      .withSprint("sprint2", new Date(), new Date())
        .withTask()
          .effort(10)
          .name("task4")
          .owner("ruben@gmail.com")
          .status("Closed")
        .addToSprint()
        .withTask()
          .effort(10)
          .name("task5")
          .owner("juana@gmail.com")
          .status("Closed")
        .addToSprint()
        .withTask()
          .effort(10)
          .name("task6")
          .owner("jose@gmail.com")
        .addToSprint()
      .addToRelease()
      .withBacklog()
        .withTask()
          .effort(10)
          .name("task7")
          .owner(null)
          .status("Closed")
        .addToSprint()
        .withTask()
          .effort(10)
          .name("task8")
          .owner(null)
          .status("Closed")           
            .addSubTask()
              .effort(20)
              .name("subtask1")
              .owner("jose@gmail.com")
              .status("Closed") 
              .addToTask()
            .addSubTask()
              .effort(20)
              .name("subtask2")
              .owner("jose@gmail.com")
              .status("Closed")  
              .addToTask()
        .addToSprint()
      .addToRelease()
    .addToProject()
    .addToProduct().addToOrganization().addToSnapshot();
    
    Snapshot snapshot = snapshotBuilder.build();

    snapshotRepository.save(snapshot);

    
    Set<Release> releases = snapshotRepository.findOne(snapshot.getId()).getReleases();
    assertThat(releases, hasSize(1));

    List<Sprint> sprints = sprintRepository.findByRelease(releases.iterator().next());
    assertThat(sprints, hasSize(2));
    Sprint sprint1 = sprints.stream().filter(s -> "sprint1".equals(s.getName())).findAny().get();
    Sprint sprint2 = sprints.stream().filter(s -> "sprint2".equals(s.getName())).findAny().get();

    List<Task> taskbacklog = taskRepository.findByReleaseAndSprint(
        releases.iterator().next(), null);
    assertThat(taskbacklog, hasSize(4));

    List<Task> taskssprint1 = taskRepository.findByReleaseAndSprint(
        releases.iterator().next(), sprints.get(0));
    assertThat(taskssprint1, hasSize(3));

    List<Task> taskssprint2 = taskRepository.findByReleaseAndSprint(
        releases.iterator().next(), sprints.get(1));
    assertThat(taskssprint2, hasSize(3));

    Pod pod1 = podRepository.findByName("POD1").iterator().next();
    assertThat(pod1, notNullValue());

    List<PodMember> podMembers = podMemberRepository.findByPod(pod1);
    assertThat(podMembers, hasSize(2));

    Pod pod2 = podRepository.findByName("POD2").iterator().next();
    assertThat(pod2, notNullValue());

    podMembers = podMemberRepository.findByPod(pod2);
    assertThat(podMembers, hasSize(2));
    
    
    QProjectMetric qpm = QProjectMetric.projectMetric;
    
    BooleanExpression projectQuery = qpm.project().name.eq("Proj prueba");
    ProjectMetric pm = projectMetricRepository.findOne(projectQuery);
    assertThat(pm, notNullValue());
    assertThat(pm.getRemainingStoryPoints(), equalTo(40));
    assertThat(pm.getActualPercentComplete(), equalTo(0.6));
    
    QSprintPodMetric qspm = QSprintPodMetric.sprintPodMetric;

    BooleanExpression pod1Sp1Query = qspm.pod().eq(pod1).and(qspm.sprint().eq(sprint1));
    SprintPodMetric pod1Sp1m = sprintPodMetricRepository.findOne(pod1Sp1Query);
    assertThat(pod1Sp1m.getPod(), equalTo(pod1));
    assertThat(pod1Sp1m.getAcceptedStoryPoints(), equalTo(30));
    
    BooleanExpression pod1Sp2Query = qspm.pod().eq(pod1).and(qspm.sprint().eq(sprint2));
    SprintPodMetric pod1Sp2m = sprintPodMetricRepository.findOne(pod1Sp2Query);
    assertThat(pod1Sp2m.getPod(), equalTo(pod1));
    assertThat(pod1Sp2m.getAcceptedStoryPoints(), equalTo(10));
    
    BooleanExpression pod2Sp1Query = qspm.pod().eq(pod2).and(qspm.sprint().eq(sprint1));
    SprintPodMetric pod2Sp1m = sprintPodMetricRepository.findOne(pod2Sp1Query);
    assertThat(pod2Sp1m.getPod(), equalTo(pod2));
    assertThat(pod2Sp1m.getAcceptedStoryPoints(), equalTo(0));
    
    BooleanExpression pod2Sp2Query = qspm.pod().eq(pod2).and(qspm.sprint().eq(sprint2));
    SprintPodMetric pod2Sp2m = sprintPodMetricRepository.findOne(pod2Sp2Query);
    assertThat(pod2Sp2m.getPod(), equalTo(pod2));
    assertThat(pod2Sp2m.getAcceptedStoryPoints(), equalTo(20));
  }
}
