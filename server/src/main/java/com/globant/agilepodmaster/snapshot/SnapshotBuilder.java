package com.globant.agilepodmaster.snapshot;

import com.globant.agilepodmaster.organization.Organization;
import com.globant.agilepodmaster.organization.OrganizationBuilder;
import com.globant.agilepodmaster.pod.Pod;
import com.globant.agilepodmaster.pod.PodBuilder;
import com.globant.agilepodmaster.pod.PodMember;
import com.globant.agilepodmaster.pod.PodsBuilder;
import com.globant.agilepodmaster.product.Product;
import com.globant.agilepodmaster.project.Project;
import com.globant.agilepodmaster.release.Release;
import com.globant.agilepodmaster.sprint.Sprint;
import com.globant.agilepodmaster.sync.SyncContext;
import com.globant.agilepodmaster.sync.jira.ReleasesBuilder;
import com.globant.agilepodmaster.task.Task;

import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import lombok.Getter;

/**
 * Creates a Snapshot in the DB.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@SuppressWarnings("PMD.TooManyMethods")
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
    Assert.notNull(context, "syncContext cannot be null");
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
    this.createMetrics();
    return snapshot;
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
  
  private void createMetrics() {
    
    MetricsGenerator metricsGenerator = new MetricsGenerator();
    
    List<SprintPodMetric> sprintPodMetrics = metricsGenerator
        .generatesSprintPodMetrics(snapshot.getPods(), snapshot.getSprints(),
            snapshot.getTasks());
    
    snapshot.addSprintMetrics(sprintPodMetrics);
    
    List<ProjectMetric> projectMetrics = metricsGenerator
        .generatesProjectMetrics(snapshot.getProjects(),
            snapshot.getTasks());
    
    snapshot.addProjectMetrics(projectMetrics);
    
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
