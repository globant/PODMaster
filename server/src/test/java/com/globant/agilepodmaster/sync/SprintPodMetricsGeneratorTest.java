package com.globant.agilepodmaster.sync;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import com.globant.agilepodmaster.AbstractUnitTest;
import com.globant.agilepodmaster.core.Organization;
import com.globant.agilepodmaster.core.Pod;
import com.globant.agilepodmaster.core.PodMember;
import com.globant.agilepodmaster.core.Product;
import com.globant.agilepodmaster.core.Project;
import com.globant.agilepodmaster.core.Release;
import com.globant.agilepodmaster.core.Sprint;
import com.globant.agilepodmaster.core.SprintPodMetric;
import com.globant.agilepodmaster.core.Task;
import com.globant.agilepodmaster.core.TaskBuilder;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Test SprintPodMetricsGenerator.
 * @author jose.dominguez@globant.com
 *
 */
public class SprintPodMetricsGeneratorTest  extends AbstractUnitTest {

  private Release release;
  
  private Sprint sprint;

  private Pod pod;
  
  private PodMember podMember1;
  private PodMember podMember2;
  
  @Autowired
  private SprintPodMetricsGenerator sprintPodMetricsGenerator;
      
  /**
   * Setting sprint, pod, members, release.
   */
  @Before
  public void setUp() {

    Organization organization = new Organization("Organization1");
    Product product = new Product("Product1", organization);
    Project project = new Project("Project1", product);
    release = new Release("Release1", project);
    sprint = new Sprint("Sprint1", release, new Date(), new Date());

    pod = new Pod("Pod1");

    podMember1 = new PodMember("Jose", "Dominguez", "jose", pod);
    podMember2 = new PodMember("Andres", "Postiglioni", "posti", pod);
  }

  /**
   * Test with added tasks.
   */
  @Test
  public void testShouldGenerateWithAddedTasks() {

    TaskBuilder<BacklogBuilder, TaskBuilder<?, ?>> builder = TaskBuilder
        .taskBuilder(release, sprint, null);
    builder.effort(90f);
    builder.owner("jose");
    Task task1 = builder.getTask();
    task1.setOwner(podMember1);
    
    builder = TaskBuilder.taskBuilder(release, sprint, null);
    builder.effort(30f);
    builder.addedDuringSprint();
    builder.name("task2");
    builder.status("closed");
    Task task2 = builder.getTask();
    task2.setOwner(podMember2);

    List<SprintPodMetric> result = sprintPodMetricsGenerator.generates(
        new HashSet<Pod>(Arrays.asList(pod)),
        new HashSet<Sprint>(Arrays.asList(sprint)),
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
        .taskBuilder(release, sprint, null);
    builder.effort(60f);
    builder.owner("jose");
    Task task1 = builder.getTask();
    task1.setOwner(podMember1);
    
    builder = TaskBuilder.taskBuilder(release, sprint, null);
    builder.effort(30f);
    builder.removedDuringSprint();
    builder.name("task2");
    Task task2 = builder.getTask();
    task2.setOwner(podMember2);

    List<SprintPodMetric> result = sprintPodMetricsGenerator.generates(
        new HashSet<Pod>(Arrays.asList(pod)),
        new HashSet<Sprint>(Arrays.asList(sprint)),    
        new HashSet<Task>(Arrays.asList(task1, task2))); 
    
    assertThat(result, hasSize(1));
    assertThat(result.get(0).getAcceptedStoryPoints(), equalTo(60));
    assertThat(result.get(0).getPlannedStoryPoints(), equalTo(90));
    assertThat(result.get(0).getEstimationAccuracy(), equalTo(1.5));
  }

}
