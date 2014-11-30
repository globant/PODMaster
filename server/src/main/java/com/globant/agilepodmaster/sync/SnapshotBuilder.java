package com.globant.agilepodmaster.sync;

import com.globant.agilepodmaster.core.Organization;
import com.globant.agilepodmaster.core.Pod;
import com.globant.agilepodmaster.core.PodMember;
import com.globant.agilepodmaster.core.Product;
import com.globant.agilepodmaster.core.Project;
import com.globant.agilepodmaster.core.ProjectPodMetric;
import com.globant.agilepodmaster.core.Release;
import com.globant.agilepodmaster.core.Snapshot;
import com.globant.agilepodmaster.core.Sprint;
import com.globant.agilepodmaster.core.SprintPodMetric;
import com.globant.agilepodmaster.core.Task;
import com.globant.agilepodmaster.sync.reading.PodsBuilder;
import com.globant.agilepodmaster.sync.reading.ReleasesBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

/**
 * Creates a Snapshot in the DB.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public class SnapshotBuilder implements PodsBuilder, ReleasesBuilder {
  private Snapshot snapshot;

  @Getter
  private SyncContext syncContext;

  private List<SnapshotDataCollector> builders = new LinkedList<SnapshotDataCollector>();

  private Map<String, List<Task>> tasks = new HashMap<String, List<Task>>();

  /**
   * Constructor.
   * 
   * @param context the context where the process will log.
   */
  public SnapshotBuilder(SyncContext context) {
    this.syncContext = context;
    this.snapshot = new Snapshot("Snapshot " + new Date());
  }

  /**
   * Assign owner to tasks, calculates metrics, associates entities to snapshot.
   * 
   * @return the snapshot.
   */
  public Snapshot build() {
    builders.forEach(b -> b.collect(this));
    snapshot.setCreationDate(new Date());
    this.assignOwnersToTasks();
    this.createSprintPodMetrics();
    this.createProjectMetrics();
    return snapshot;
  }

  private void createProjectMetrics() {
    
    //TODO not necessary to be assigned and not assigned to closed sprints.
    for (Project project : snapshot.getProjects()) {
      for (Pod pod : snapshot.getPods()) {
        int remainingStoryPoints = (int) snapshot.getTasks().stream()
            .filter(t -> project.equals(t.getProject()))
            .filter(t -> t.getOwner() != null)
            .filter(t -> pod.equals(t.getOwner().getPod()))
            .filter(t -> t.isOpen()).mapToDouble(Task::getEffort).sum();

        ProjectPodMetric projectMetric = new ProjectPodMetric(project, pod);
        projectMetric.setRemainingStoryPoints(remainingStoryPoints);

        snapshot.addProjectMetric(projectMetric);
      }
    }
  }

  private void assignOwnersToTasks() {
    for (Entry<String, List<Task>> entry : tasks.entrySet()) {
      String ownerEmail = entry.getKey();
      List<Task> taskList = entry.getValue();
      try {
        PodMember podMember = snapshot.getPodMembers().stream()
            .filter(m -> m.getEmail().equals(ownerEmail)).findAny().get();
        taskList.forEach(t -> t.setOwner(podMember));
      } catch (NoSuchElementException ne) {
        StringBuilder taskNameList = new StringBuilder();
        taskList.forEach(t -> taskNameList.append(t.getName()).append(" - "));
        syncContext.warn("Task owner not found: " + ownerEmail + "for tasks: "
            + taskNameList);
      }

      taskList.forEach(t -> snapshot.addTask(t));
    }
  }

  private void createSprintPodMetrics() {
    
    SprintPodMetricsGenerator sprintPodMetricsGenerator = new SprintPodMetricsGenerator();
    
    List<SprintPodMetric> sprintPodMetrics = sprintPodMetricsGenerator
        .generates(snapshot.getPods(), snapshot.getSprints(),
            snapshot.getTasks());
    
    snapshot.addSprintMetrics(sprintPodMetrics);
    
  }

  @Override
  public PodBuilder withPod(String name) {
    PodBuilder podBuilder = new PodBuilder(name, this);
    builders.add(podBuilder);
    return podBuilder;
  }

  @Override
  public OrganizationBuilder withOrganization(String organizationName) {
    OrganizationBuilder builder = new OrganizationBuilder(organizationName,
        this);
    builders.add(builder);
    return builder;
  }

  /**
   * Add an organization to the snapshot.
   * 
   * @param organization the organization
   */
  public void addOrganization(Organization organization) {
    snapshot.addOrganization(organization);
  }

  /**
   * Add a project to the snapshot.
   * 
   * @param project the project.
   */
  public void addProject(Project project) {
    snapshot.addProject(project);
  }

  /**
   * Add a product to the snapshot.
   * 
   * @param product the product.
   */
  public void addProduct(Product product) {
    snapshot.addProduct(product);
  }

  /**
   * Add a release to the snapshot.
   * 
   * @param release the release.
   */
  public void addRelease(Release release) {
    snapshot.addRelease(release);
  }

  /**
   * Add a sprint to the snapshot.
   * 
   * @param sprint the sprint.
   */
  public void addSprint(Sprint sprint) {
    snapshot.addSprint(sprint);
  }

  /**
   * Add a owner to the snapshot tasks and associates the task.
   * 
   * @param owner the owner.
   * @param task the task.
   */
  public void addTaskAssignedTo(String owner, Task task) {
    tasks.putIfAbsent(owner, new LinkedList<Task>());
    tasks.get(owner).add(task);
  }

  /**
   * Add the pod to the snapshot.
   * 
   * @param pod the pod.
   */
  public void addPod(Pod pod) {
    snapshot.addPod(pod);
  }

  /**
   * Add the pod member to the snapshot.
   * 
   * @param podMember the pod member.
   */
  public void addPodMember(PodMember podMember) {
    snapshot.addPodMember(podMember);
  }
}
