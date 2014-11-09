package com.globant.agilepodmaster.sync;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import com.globant.agilepodmaster.AbstractUnitTest;
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
import com.globant.agilepodmaster.core.Release;
import com.globant.agilepodmaster.core.ReleaseRepository;
import com.globant.agilepodmaster.core.Snapshot;
import com.globant.agilepodmaster.core.SnapshotRepository;
import com.globant.agilepodmaster.core.Sprint;
import com.globant.agilepodmaster.core.SprintRepository;
import com.globant.agilepodmaster.core.Task;
import com.globant.agilepodmaster.core.TaskRepository;
import com.globant.agilepodmaster.sync.reading.PodMemberDTO;
import com.globant.agilepodmaster.sync.reading.TaskDTO;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Testing Snapshot building.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public class SnapshotBuilderTest extends AbstractUnitTest {

  @Autowired
  SnapshotBuilder snapshotBuilder;

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

  /**
   * Testing AddWith method. Adding Task with Parent
   */
  @Test
  public void testAddWithParent() {

    Organization organization = new Organization("Org Prueba");
    Product product = new Product("Prod Prueba", organization);
    Project project = new Project("Proj Prueba", product);
    Snapshot snapshot = new Snapshot("Snap Prueba");
    Release release = new Release("Rel Prueba", snapshot, project);
    Sprint sprint = new Sprint("Sprint Prueba", release, new Date(), new Date());
    SyncContext context = new SyncContext();

    TaskDTO taskDTO1 = new TaskDTO.Builder(context).name("task1").build();

    List<Task> taskList = new ArrayList<Task>();
    snapshotBuilder.addTask(taskDTO1, null, release, sprint, taskList);

    Task task1 = taskList.get(0);
    assertThat(task1.getName(), is("task1"));
    assertThat(task1.getParentTask(), nullValue());

    TaskDTO taskDTO2 = new TaskDTO.Builder(context).name("task2").build();

    snapshotBuilder.addTask(taskDTO2, task1, release, sprint, taskList);

    Task task2 = taskList.get(1);
    assertThat(task2.getName(), is("task2"));
    assertThat(task2.getParentTask(), equalTo(task1));
    assertThat(task2.getSprint().getName(), equalTo("Sprint Prueba"));

  }

  /**
   * Testing AddWith method. Adding Task with Subtasks.
   */
  @Test
  public void testAddTaskWithSubTasks() {

    Organization organization = new Organization("Org Prueba");
    Product product = new Product("Prod Prueba", organization);
    Project project = new Project("Proj Prueba", product);
    Snapshot snapshot = new Snapshot("Snap Prueba");
    Release release = new Release("Rel Prueba", snapshot, project);
    Sprint sprint = new Sprint("Sprint Prueba", release, new Date(), new Date());
    SyncContext context = new SyncContext();

    TaskDTO taskDTO1 = new TaskDTO.Builder(context).name("task1").build();
    TaskDTO taskDTO2 = new TaskDTO.Builder(context).name("task2").build();
    TaskDTO taskDTO3 = new TaskDTO.Builder(context).name("task3").build();


    List<TaskDTO> subTasks = new ArrayList<TaskDTO>();
    subTasks.add(taskDTO2);
    subTasks.add(taskDTO3);

    taskDTO1.setSubTasks(subTasks);

    List<Task> taskList = new ArrayList<Task>();
    snapshotBuilder.addTask(taskDTO1, null, release, sprint, taskList);

    Task task1 = taskList.get(0);
    assertThat(task1.getName(), is("task1"));

    Task task2 = taskList.get(1);
    assertThat(task2.getName(), is("task2"));

    Task task3 = taskList.get(2);
    assertThat(task3.getName(), is("task3"));

    assertThat(task2.getParentTask(), equalTo(task1));
    assertThat(task3.getParentTask(), equalTo(task1));

  }

  /**
   * Testing AddWith method. Adding Task with Member.
   */
  @Test
  public void testAddTaskWithMember() {
    Organization organization = new Organization("Org Prueba");
    Product product = new Product("Prod Prueba", organization);
    Project project = new Project("Proj Prueba", product);
    Snapshot snapshot = new Snapshot("Snap Prueba");
    Release release = new Release("Rel Prueba", snapshot, project);
    Sprint sprint = new Sprint("Sprint Prueba", release, new Date(), new Date());
    SyncContext context = new SyncContext();

    TaskDTO taskDTO1 = new TaskDTO.Builder(context).name("task1")
        .owner("jose@gmail.com").build();

    PodMemberDTO memberDTO1 = new PodMemberDTO("jose@gmail.com", "Jose",
        "Dominguez", "POD1");
    Map<String, PodMemberDTO> podMembersMap = new HashMap<String, PodMemberDTO>();
    podMembersMap.put("jose@gmail.com", memberDTO1);
    snapshotBuilder.addMembersMap(podMembersMap);

    List<Task> taskList = new ArrayList<Task>();
    snapshotBuilder.addTask(taskDTO1, null, release, sprint, taskList);

    Task task1 = taskList.get(0);
    assertThat(task1.getName(), is("task1"));
    assertThat(task1.getOwner(), notNullValue());

    PodMember podMember = task1.getOwner();
    assertThat(podMember.getEmail(), is("jose@gmail.com"));
    assertThat(podMember.getFirstName(), is("Jose"));
    assertThat(podMember.getLastName(), is("Dominguez"));

    Pod pod = podMember.getPod();
    assertThat(pod.getName(), is("POD1"));

  }

  /**
   * Test what was store in the DB.
   */
  @Test
  public void testBuild() {

    Organization organization = new Organization("Org Prueba");
    Product product = new Product("Prod Prueba", organization);
    Project project = new Project("Proj Prueba", product);

    organizationRepository.save(organization);
    product = productRepository.save(product);
    project = projectRepository.save(project);

//    List<Project> list = (List<Project>) projectRepository.findAll();

    SyncContext context = new SyncContext();

    TaskDTO task1 = new TaskDTO.Builder(context).name("task1")
        .owner("jose@gmail.com").build();

    TaskDTO task2 = new TaskDTO.Builder(context).name("task2")
        .owner("maria@gmail.com").build();

    TaskDTO task3 = new TaskDTO.Builder(context).name("task3")
        .owner("ruben@gmail.com").build();

    TaskDTO task4 = new TaskDTO.Builder(context).name("task4")
        .owner("juana@gmail.com").build();

    TaskDTO task5 = new TaskDTO.Builder(context).name("task5").build();

    TaskDTO task6 = new TaskDTO.Builder(context).name("task6").build();

    List<TaskDTO> taskSprint1 = new ArrayList<TaskDTO>();
    taskSprint1.add(task1);
    taskSprint1.add(task2);

    List<TaskDTO> taskSprint2 = new ArrayList<TaskDTO>();
    taskSprint2.add(task3);
    taskSprint2.add(task4);

    List<TaskDTO> backlog = new ArrayList<TaskDTO>();
    backlog.add(task5);
    backlog.add(task6);

    PodMemberDTO memberDTO1 = new PodMemberDTO("jose@gmail.com", "Jose",
        "Dominguez", "POD1");
    PodMemberDTO memberDTO2 = new PodMemberDTO("maria@gmail.com", "Maria",
        "Gomez", "POD1");
    PodMemberDTO memberDTO3 = new PodMemberDTO("ruben@gmail.com", "Ruben",
        "Dartes", "POD2");
    Map<String, PodMemberDTO> podMembersMap = new HashMap<String, PodMemberDTO>();
    podMembersMap.put("jose@gmail.com", memberDTO1);
    podMembersMap.put("maria@gmail.com", memberDTO2);
    podMembersMap.put("ruben@gmail.com", memberDTO3);

    snapshotBuilder.withProduct(product).addMembersMap(podMembersMap)
        .addRelease("release1", project.getId())
        .addSprint("sprint1", new Date(), new Date(), taskSprint1)
        .addSprint("sprint2", new Date(), new Date(), taskSprint2)
        .addBacklog(backlog).build();

    Snapshot snapshot = snapshotRepository.findAll().iterator().next();

    assertThat(snapshot.getProduct(), equalTo(product));

    List<Release> releases = releaseRepository.findBySnapshot(snapshot);
    assertThat(releases, hasSize(1));

    List<Sprint> sprints = sprintRepository.findByRelease(releases.get(0));
    assertThat(sprints, hasSize(2));

    List<Task> taskbacklog = taskRepository.findByReleaseAndSprint(
        releases.get(0), null);
    assertThat(taskbacklog, hasSize(2));
    
    List<Task> taskssprint1 = taskRepository.findByReleaseAndSprint(
        releases.get(0), sprints.get(0));
    assertThat(taskssprint1, hasSize(2));

    List<Task> taskssprint2 = taskRepository.findByReleaseAndSprint(
        releases.get(0), sprints.get(1));
    assertThat(taskssprint2, hasSize(2));

    List<Pod> pods = podRepository.findByName("POD1");
    assertThat(pods, notNullValue());

    List<PodMember> podMembers = podMemberRepository.findByPod(pods.get(0));
    assertThat(podMembers, hasSize(2));

    pods = podRepository.findByName("POD2");
    assertThat(pods, hasSize(1));

    podMembers = podMemberRepository.findByPod(pods.get(0));
    assertThat(podMembers, hasSize(1));

  }

}