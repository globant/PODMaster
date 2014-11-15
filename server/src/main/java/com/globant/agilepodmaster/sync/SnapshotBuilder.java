package com.globant.agilepodmaster.sync;

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

import com.globant.agilepodmaster.core.Organization;
import com.globant.agilepodmaster.core.Pod;
import com.globant.agilepodmaster.core.PodMember;
import com.globant.agilepodmaster.core.Product;
import com.globant.agilepodmaster.core.Project;
import com.globant.agilepodmaster.core.Release;
import com.globant.agilepodmaster.core.Snapshot;
import com.globant.agilepodmaster.core.Sprint;
import com.globant.agilepodmaster.core.SprintPodMetric;
import com.globant.agilepodmaster.core.Task;

/**
 * Creates a Snapshot in the DB.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public class SnapshotBuilder {
  private Snapshot snapshot;

  @Getter
  private SyncContext syncContext;

  private List<SnapshotDataCollector> builders = new LinkedList<SnapshotDataCollector>();

  private Map<Task, String> tasks = new HashMap<Task, String>();

  /**
   * Constructor.
   */
  public SnapshotBuilder(SyncContext context) {
    this.syncContext = context;
    this.snapshot = new Snapshot("Snapshot " + new Date());
  }

  public Snapshot build() {
    builders.forEach(b -> b.collect(this));
    snapshot.setCreationDate(new Date());
    this.assignOwnersToTasks();
    this.createSprintPodMetrics();
    return snapshot;
  }

  private void assignOwnersToTasks() {
    for (Entry<Task, String> entry : tasks.entrySet()) {
      String ownerEmail = entry.getValue();
      Task task = entry.getKey();
      try {
        PodMember podMember = snapshot.getPodMembers().stream()
            .filter(m -> m.getEmail().equals(ownerEmail)).findAny().get();
        task.setOwner(podMember);
      } catch (NoSuchElementException ne) {
        syncContext.warn("Task owner not found: " + ownerEmail);
      }
    }
  }

  private void createSprintPodMetrics() {
    for (Pod pod : snapshot.getPods()) {
      for (Sprint sprint : snapshot.getSprints()) {
        double velocity = 
            streamFor(pod, sprint).filter(t -> t.isAccepted()).mapToDouble(Task::getEffort).sum();
        
        double plannedEffort = streamFor(pod, sprint).mapToDouble(Task::getEffort).sum();

        SprintPodMetric spm = new SprintPodMetric(sprint, pod);
        spm.setAcceptedStoryPoints((int) velocity);
        spm.setPlannedStoryPoints((int) plannedEffort);
        
        snapshot.addSprintMetric(spm);
      }
    }
  }

  private Stream<Task> streamFor(Pod pod, Sprint sprint) {
    return snapshot.getTasks().stream()
    .filter(t -> sprint.equals(t.getSprint()) && t.getOwner() != null 
                 && pod.equals(t.getOwner().getPod()))
    .collect(Collectors.toList()).stream();
  }

  public PodBuilder withPod(String name) {
    PodBuilder podBuilder = new PodBuilder(name, this);
    builders.add(podBuilder);
    return podBuilder;
  }

  public OrganizationBuilder withOrganization(String organizationName) {
    OrganizationBuilder builder = new OrganizationBuilder(organizationName, this);
    builders.add(builder);
    return builder;
  }

  public void addOrganization(Organization organization) {
    snapshot.addOrganization(organization);
  }

  public void addProject(Project project) {
    snapshot.addProject(project);
  }

  public void addProduct(Product product) {
    snapshot.addProduct(product);
  }

  public void addRelease(Release release) {
    snapshot.addRelease(release);
  }

  public void addSprint(Sprint sprint) {
    snapshot.addSprint(sprint);
  }

  public void addTaskAssignedTo(String owner, Task task) {
    tasks.put(task, owner);
    snapshot.addTask(task);
  }

  public void addPod(Pod pod) {
    snapshot.addPod(pod);
  }

  public void addPodMember(PodMember podMember) {
    snapshot.addPodMember(podMember);
  }
}
