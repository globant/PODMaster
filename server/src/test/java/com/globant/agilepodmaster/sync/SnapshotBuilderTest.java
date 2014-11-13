package com.globant.agilepodmaster.sync;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.globant.agilepodmaster.AbstractUnitTest;
import com.globant.agilepodmaster.core.OrganizationRepository;
import com.globant.agilepodmaster.core.Pod;
import com.globant.agilepodmaster.core.PodMember;
import com.globant.agilepodmaster.core.PodMemberRepository;
import com.globant.agilepodmaster.core.PodRepository;
import com.globant.agilepodmaster.core.ProductRepository;
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
import com.globant.agilepodmaster.sync.reading.TaskDTO;
import com.mysema.query.types.expr.BooleanExpression;


/**
 * Testing Snapshot building.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Transactional
public class SnapshotBuilderTest extends AbstractUnitTest {

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

//  /**
//   * Testing AddWith method. Adding Task with Parent
//   */
//  @Test
//  public void testAddWithParent() {
//    Organization organization = new Organization("Org Prueba");
//    Product product = new Product("Prod Prueba", organization);
//    Project project = new Project("Proj Prueba", product);
//    Snapshot snapshot = new Snapshot("Snap Prueba");
//    Release release = new Release("Rel Prueba", snapshot, project);
//    Sprint sprint = new Sprint("Sprint Prueba", release, new Date(), new Date());
//    SyncContext context = new SyncContext();
//
//    TaskDTO taskDTO1 = new TaskDTO.Builder(context).name("task1").build();
//
//    List<Task> taskList = new ArrayList<Task>();
//    snapshotBuilder.addTask(taskDTO1, null, release, sprint, taskList);
//
//    Task task1 = taskList.get(0);
//    assertThat(task1.getName(), is("task1"));
//    assertThat(task1.getParentTask(), nullValue());
//
//    TaskDTO taskDTO2 = new TaskDTO.Builder(context).name("task2").build();
//
//    snapshotBuilder.addTask(taskDTO2, task1, release, sprint, taskList);
//
//    Task task2 = taskList.get(1);
//    assertThat(task2.getName(), is("task2"));
//    assertThat(task2.getParentTask(), equalTo(task1));
//    assertThat(task2.getSprint().getName(), equalTo("Sprint Prueba"));
//  }
//
//  /**
//   * Testing AddWith method. Adding Task with Subtasks.
//   */
//  @Test
//  public void testAddTaskWithSubTasks() {
//
//    Organization organization = new Organization("Org Prueba");
//    Product product = new Product("Prod Prueba", organization);
//    Project project = new Project("Proj Prueba", product);
//    Snapshot snapshot = new Snapshot("Snap Prueba");
//    Release release = new Release("Rel Prueba", snapshot, project);
//    Sprint sprint = new Sprint("Sprint Prueba", release, new Date(), new Date());
//    SyncContext context = new SyncContext();
//
//    TaskDTO taskDTO1 = new TaskDTO.Builder(context).name("task1").build();
//    TaskDTO taskDTO2 = new TaskDTO.Builder(context).name("task2").build();
//    TaskDTO taskDTO3 = new TaskDTO.Builder(context).name("task3").build();
//
//
//    List<TaskDTO> subTasks = new ArrayList<TaskDTO>();
//    subTasks.add(taskDTO2);
//    subTasks.add(taskDTO3);
//
//    taskDTO1.setSubTasks(subTasks);
//
//    List<Task> taskList = new ArrayList<Task>();
//    snapshotBuilder.addTask(taskDTO1, null, release, sprint, taskList);
//
//    Task task1 = taskList.get(0);
//    assertThat(task1.getName(), is("task1"));
//
//    Task task2 = taskList.get(1);
//    assertThat(task2.getName(), is("task2"));
//
//    Task task3 = taskList.get(2);
//    assertThat(task3.getName(), is("task3"));
//
//    assertThat(task2.getParentTask(), equalTo(task1));
//    assertThat(task3.getParentTask(), equalTo(task1));
//  }
//
//  /**
//   * Testing AddWith method. Adding Task with Member.
//   */
//  @Test
//  public void testAddTaskWithMember() {
//    Organization organization = new Organization("Org Prueba");
//    Product product = new Product("Prod Prueba", organization);
//    Project project = new Project("Proj Prueba", product);
//    Snapshot snapshot = new Snapshot("Snap Prueba");
//    Release release = new Release("Rel Prueba", snapshot, project);
//    Sprint sprint = new Sprint("Sprint Prueba", release, new Date(), new Date());
//    SyncContext context = new SyncContext();
//
//    TaskDTO taskDTO1 = new TaskDTO.Builder(context).name("task1")
//        .owner("jose@gmail.com").build();
//
//    PodMemberDTO memberDTO1 = new PodMemberDTO("jose@gmail.com", "Jose",
//        "Dominguez", "POD1");
//    Map<String, PodMemberDTO> podMembersMap = new HashMap<String, PodMemberDTO>();
//    podMembersMap.put("jose@gmail.com", memberDTO1);
//    snapshotBuilder.addMembersMap(podMembersMap);
//
//    List<Task> taskList = new ArrayList<Task>();
//    snapshotBuilder.addTask(taskDTO1, null, release, sprint, taskList);
//
//    Task task1 = taskList.get(0);
//    assertThat(task1.getName(), is("task1"));
//    assertThat(task1.getOwner(), notNullValue());
//
//    PodMember podMember = task1.getOwner();
//    assertThat(podMember.getEmail(), is("jose@gmail.com"));
//    assertThat(podMember.getFirstName(), is("Jose"));
//    assertThat(podMember.getLastName(), is("Dominguez"));
//
//    Pod pod = podMember.getPod();
//    assertThat(pod.getName(), is("POD1"));
//
//  }

  /**
   * Test what was store in the DB.
   */
  @Test
  public void testBuild() {
//    List<Project> list = (List<Project>) projectRepository.findAll();

    SyncContext context = new SyncContext();

    SnapshotBuilder snapshotBuilder = new SnapshotBuilder(context);

    //Sprint 1
    TaskDTO task1 = new TaskDTO.Builder(context).name("task1").owner("jose@gmail.com").actual(10).status("Closed").build(); //pod1
    TaskDTO task2 = new TaskDTO.Builder(context).name("task2").owner("maria@gmail.com").actual(10).status("Closed").build(); //pod1
    TaskDTO task3 = new TaskDTO.Builder(context).name("task3").owner("maria@gmail.com").actual(10).build(); //pod1

    //Sprint 2
    TaskDTO task4 = new TaskDTO.Builder(context).name("task4").owner("ruben@gmail.com").actual(10).status("Closed").build(); //pod2
    TaskDTO task5 = new TaskDTO.Builder(context).name("task5").owner("juana@gmail.com").actual(10).status("Closed").build(); //pod2
    TaskDTO task7 = new TaskDTO.Builder(context).name("task7").status("Closed").actual(10).build(); //no pod

    //Backlog
    TaskDTO task8 = new TaskDTO.Builder(context).name("task8").status("Closed").actual(10).build(); //no pod
    TaskDTO task6 = new TaskDTO.Builder(context).name("task5").owner("juana@gmail.com").actual(10).build(); //pod 2

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
       .addProject("Proj prueba")
        .withRelease("release1")
        .withSprint("sprint1", new Date(), new Date())
        .withTask()
          .actual(task1.getActual())
          .createDate(task1.getCreateDate())
          .effort(task1.getEffort())
          .estimated(task1.getEstimated())
          .name(task1.getName())
          .owner(task1.getOwner())
          .priority(task1.getPriority().toString())
          .remaining(task1.getRemaining())
          .severity(task1.getSeverity().toString())
          .status(task1.getStatus().toString())
        .addToSprint()
        .withTask()
          .actual(task2.getActual())
          .createDate(task2.getCreateDate())
          .effort(task2.getEffort())
          .estimated(task2.getEstimated())
          .name(task2.getName())
          .owner(task2.getOwner())
          .priority(task2.getPriority().toString())
          .remaining(task2.getRemaining())
          .severity(task2.getSeverity().toString())
          .status(task2.getStatus().toString())
        .addToSprint()
        .withTask()
          .actual(task3.getActual())
          .createDate(task3.getCreateDate())
          .effort(task3.getEffort())
          .estimated(task3.getEstimated())
          .name(task3.getName())
          .owner(task3.getOwner())
          .priority(task3.getPriority().toString())
          .remaining(task3.getRemaining())
          .severity(task3.getSeverity().toString())
          .status(task3.getStatus().toString())
        .addToSprint()
      .addToRelease()
      .withSprint("sprint2", new Date(), new Date())
        .withTask()
          .actual(task4.getActual())
          .createDate(task4.getCreateDate())
          .effort(task4.getEffort())
          .estimated(task4.getEstimated())
          .name(task4.getName())
          .owner(task4.getOwner())
          .priority(task4.getPriority().toString())
          .remaining(task4.getRemaining())
          .severity(task4.getSeverity().toString())
          .status(task4.getStatus().toString())
        .addToSprint()
        .withTask()
          .actual(task5.getActual())
          .createDate(task5.getCreateDate())
          .effort(task5.getEffort())
          .estimated(task5.getEstimated())
          .name(task5.getName())
          .owner(task5.getOwner())
          .priority(task5.getPriority().toString())
          .remaining(task5.getRemaining())
          .severity(task5.getSeverity().toString())
          .status(task5.getStatus().toString())
        .addToSprint()
        .withTask()
          .actual(task6.getActual())
          .createDate(task6.getCreateDate())
          .effort(task6.getEffort())
          .estimated(task6.getEstimated())
          .name(task6.getName())
          .owner(task6.getOwner())
          .priority(task6.getPriority().toString())
          .remaining(task6.getRemaining())
          .severity(task6.getSeverity().toString())
          .status(task6.getStatus().toString())
        .addToSprint()
      .addToRelease()
      .withBacklog()
        .withTask()
          .actual(task7.getActual())
          .createDate(task7.getCreateDate())
          .effort(task7.getEffort())
          .estimated(task7.getEstimated())
          .name(task7.getName())
          .owner(task7.getOwner())
          .priority(task7.getPriority().toString())
          .remaining(task7.getRemaining())
          .severity(task7.getSeverity().toString())
          .status(task7.getStatus().toString())
        .addToSprint()
        .withTask()
          .actual(task8.getActual())
          .createDate(task8.getCreateDate())
          .effort(task8.getEffort())
          .estimated(task8.getEstimated())
          .name(task8.getName())
          .owner(task8.getOwner())
          .priority(task8.getPriority().toString())
          .remaining(task8.getRemaining())
          .severity(task8.getSeverity().toString())
          .status(task8.getStatus().toString())
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
    assertThat(taskbacklog, hasSize(2));

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
    
    QSprintPodMetric qspm = QSprintPodMetric.sprintPodMetric;

    BooleanExpression pod1Sp1Query = qspm.pod.eq(pod1).and(qspm.sprint.eq(sprint1));
    SprintPodMetric pod1Sp1m = sprintPodMetricRepository.findOne(pod1Sp1Query);
    assertThat(pod1Sp1m.getPod(), equalTo(pod1));
    assertThat(pod1Sp1m.getAcceptedStoryPoints(), equalTo(20));
    
    BooleanExpression pod1Sp2Query = qspm.pod.eq(pod1).and(qspm.sprint.eq(sprint2));
    SprintPodMetric pod1Sp2m = sprintPodMetricRepository.findOne(pod1Sp2Query);
    assertThat(pod1Sp2m.getPod(), equalTo(pod1));
    assertThat(pod1Sp2m.getAcceptedStoryPoints(), equalTo(0));
    
    BooleanExpression pod2Sp1Query = qspm.pod.eq(pod2).and(qspm.sprint.eq(sprint1));
    SprintPodMetric pod2Sp1m = sprintPodMetricRepository.findOne(pod2Sp1Query);
    assertThat(pod2Sp1m.getPod(), equalTo(pod2));
    assertThat(pod2Sp1m.getAcceptedStoryPoints(), equalTo(0));
    
    BooleanExpression pod2Sp2Query = qspm.pod.eq(pod2).and(qspm.sprint.eq(sprint2));
    SprintPodMetric pod2Sp2m = sprintPodMetricRepository.findOne(pod2Sp2Query);
    assertThat(pod2Sp2m.getPod(), equalTo(pod2));
    assertThat(pod2Sp2m.getAcceptedStoryPoints(), equalTo(20));
  }
}
