package com.globant.agilepodmaster.sync;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import com.globant.agilepodmaster.AbstractUnitTest;
import com.globant.agilepodmaster.pod.Pod;
import com.globant.agilepodmaster.pod.PodMember;
import com.globant.agilepodmaster.product.Organization;
import com.globant.agilepodmaster.product.Product;
import com.globant.agilepodmaster.project.Project;
import com.globant.agilepodmaster.release.Release;
import com.globant.agilepodmaster.snapshot.MetricsGenerator;
import com.globant.agilepodmaster.snapshot.SprintPodMetric;
import com.globant.agilepodmaster.sprint.BacklogBuilder;
import com.globant.agilepodmaster.sprint.Sprint;
import com.globant.agilepodmaster.task.Task;
import com.globant.agilepodmaster.task.TaskBuilder;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Test SprintPodMetricsGenerator.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public class SprintPodMetricsGeneratorTest extends AbstractUnitTest {

  private Release release1;
  private Release release2;
  
  private Sprint sprint1;
  private Sprint sprint2;
  private Sprint sprint21;

  private Pod pod1;
  private Pod pod2;

  private PodMember podMember1;
  private PodMember podMember2;
  private PodMember podMember3;
  private PodMember podMember4;

  @Autowired
  private MetricsGenerator sprintPodMetricsGenerator;

  /**
   * Setting sprint, pod, members, release.
   */
  @Before
  public void setUp() {

    Organization organization = new Organization("Organization1");
    Product product = new Product("Product1", organization);
    Project project1 = new Project("Project1", 500, product);
    release1 = new Release("Release1", project1);
    Project project2 = new Project("Project2", 500, product);
    release2 = new Release("Release2", project2);

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -5);

    sprint1 = new Sprint("Sprint1", 1, release1, calendar.getTime(), new Date());
    sprint2 = new Sprint("Sprint2", 2, release1, new Date(), new Date());
    sprint21 = new Sprint("Sprint1", 1, release2, new Date(), new Date());

    pod1 = new Pod("Pod1");

    podMember1 = new PodMember("Jose", "Dominguez", "jose", pod1);
    podMember2 = new PodMember("Andres", "Postiglioni", "posti", pod1);

    pod2 = new Pod("Pod2");

    podMember3 = new PodMember("Pepe", "Rojas", "pepe", pod2);
    podMember4 = new PodMember("Martu", "Roldon", "martu", pod2);
  }

  /**
   * Test with added tasks.
   */
  @Test
  public void testShouldGenerateWithAddedTasks() {

    TaskBuilder<BacklogBuilder, TaskBuilder<?, ?>> builder = TaskBuilder
        .taskBuilder(release1, sprint1, null);
    builder.effort(90f);
    builder.owner("jose");
    Task task1 = builder.getTask();
    task1.setOwner(podMember1);

    builder = TaskBuilder.taskBuilder(release1, sprint1, null);
    builder.effort(30f);
    builder.addedDuringSprint();
    builder.name("task2");
    builder.status("closed");
    Task task2 = builder.getTask();
    task2.setOwner(podMember2);

    List<SprintPodMetric> result = sprintPodMetricsGenerator.generatesSprintPodMetrics(
        new HashSet<Pod>(Arrays.asList(pod1)),
        new HashSet<Sprint>(Arrays.asList(sprint1)),
        new HashSet<Task>(Arrays.asList(task1, task2)));

    assertThat(result, hasSize(1));
    assertThat(result.get(0).getAcceptedStoryPoints(), equalTo(120));
    assertThat(result.get(0).getPlannedStoryPoints(), equalTo(90));
    assertThat(result.get(0).getEstimationAccuracy(), equalTo(0.75));
  }

  /**
   * Test with removed added tasks.
   */
  @Test
  public void testShouldGenerateWithRemovedTasks() {

    TaskBuilder<BacklogBuilder, TaskBuilder<?, ?>> builder = TaskBuilder
        .taskBuilder(release1, sprint1, null);
    builder.effort(60f);
    builder.owner("jose");
    Task task1 = builder.getTask();
    task1.setOwner(podMember1);

    builder = TaskBuilder.taskBuilder(release1, sprint1, null);
    builder.effort(30f);
    builder.removedDuringSprint();
    builder.name("task2");
    Task task2 = builder.getTask();
    task2.setOwner(podMember2);

    List<SprintPodMetric> result = sprintPodMetricsGenerator.generatesSprintPodMetrics(
        new HashSet<Pod>(Arrays.asList(pod1)),
        new HashSet<Sprint>(Arrays.asList(sprint1)),
        new HashSet<Task>(Arrays.asList(task1, task2)));

    assertThat(result, hasSize(1));
    assertThat(result.get(0).getAcceptedStoryPoints(), equalTo(60));
    assertThat(result.get(0).getPlannedStoryPoints(), equalTo(90));
    assertThat(result.get(0).getEstimationAccuracy(), equalTo(1.5));
  }
  
  /**
   * Test with added tasks.
   */
  @Test
  public void testShouldCheckForAccumulatedStoryPoints() {

    TaskBuilder<BacklogBuilder, TaskBuilder<?, ?>> builder = TaskBuilder
        .taskBuilder(release1, sprint1, null);
    builder.effort(10f);
    Task task1 = builder.getTask();
    task1.setOwner(podMember1);

    builder = TaskBuilder
        .taskBuilder(release1, sprint1, null);
    builder.effort(30f);
    Task task2 = builder.getTask();
    task2.setOwner(podMember2);
    
    builder = TaskBuilder
        .taskBuilder(release1, sprint2, null);
    builder.effort(20f);
    Task task3 = builder.getTask();
    task3.setOwner(podMember1);

    builder = TaskBuilder
        .taskBuilder(release1, sprint2, null);
    builder.effort(80f);
    Task task4 = builder.getTask();
    task4.setOwner(podMember2);
    
    builder = TaskBuilder
        .taskBuilder(release1, sprint1, null);
    builder.effort(15f);
    Task task5 = builder.getTask();
    task5.setOwner(podMember3);

    builder = TaskBuilder
        .taskBuilder(release1, sprint1, null);
    builder.effort(5f);
    Task task6 = builder.getTask();
    task6.setOwner(podMember4);
    
    builder = TaskBuilder
        .taskBuilder(release1, sprint2, null);
    builder.effort(20f);
    Task task7 = builder.getTask();
    task7.setOwner(podMember3);

    builder = TaskBuilder
        .taskBuilder(release1, sprint2, null);
    builder.effort(90f);
    Task task8 = builder.getTask();
    task8.setOwner(podMember4);
    
    builder = TaskBuilder
        .taskBuilder(release2, sprint21, null);
    builder.effort(25f);
    Task task9 = builder.getTask();
    task9.setOwner(podMember1);

    builder = TaskBuilder
        .taskBuilder(release2, sprint21, null);
    builder.effort(20f);
    Task task10 = builder.getTask();
    task10.setOwner(podMember2);
   
    
    List<SprintPodMetric> result = sprintPodMetricsGenerator.generatesSprintPodMetrics(
        new HashSet<Pod>(Arrays.asList(pod1, pod2)),
        new HashSet<Sprint>(Arrays.asList(sprint2, sprint1, sprint21)),
        new HashSet<Task>(Arrays.asList(task1, task2, task3, task4, task5,
            task6, task7, task8, task9, task10)));

    assertThat(result, hasSize(6));
    assertThat(result.get(0).getAccumutaledStoryPoints(), equalTo(40));
    assertThat(result.get(1).getAccumutaledStoryPoints(), equalTo(140));
    assertThat(result.get(2).getAccumutaledStoryPoints(), equalTo(45));
    assertThat(result.get(3).getAccumutaledStoryPoints(), equalTo(20));
    assertThat(result.get(4).getAccumutaledStoryPoints(), equalTo(130));
    assertThat(result.get(5).getAccumutaledStoryPoints(), equalTo(0));

    
    
    
    
  }

}
