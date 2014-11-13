package com.globant.agilepodmaster.sync;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.globant.agilepodmaster.core.Pod;
import com.globant.agilepodmaster.core.PodMember;
import com.globant.agilepodmaster.core.Product;
import com.globant.agilepodmaster.core.Project;
import com.globant.agilepodmaster.core.ProjectRepository;
import com.globant.agilepodmaster.core.Release;
import com.globant.agilepodmaster.core.Snapshot;
import com.globant.agilepodmaster.core.Sprint;
import com.globant.agilepodmaster.core.SprintPodMetric;
import com.globant.agilepodmaster.core.Task;
import com.globant.agilepodmaster.sync.reading.PodMemberDTO;
import com.globant.agilepodmaster.sync.reading.PodsBuilder;
import com.globant.agilepodmaster.sync.reading.Reader;
import com.globant.agilepodmaster.sync.reading.ReleasesBuilder;
import com.globant.agilepodmaster.sync.reading.TaskDTO;

/**
 * Creates a Snapshot in the DB.
 * @author jose.dominguez@globant.com
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SnapshotBuilder implements PodsBuilder,  ReleasesBuilder {
  
  @Autowired
  private ProjectRepository projectRepository;

  private Snapshot snapshot; 
  
  private Product product; 

  private Map<String, PodMemberDTO> podMembersMap;
  
  @Getter
  private SyncContext syncContext;

  /**
   * Constructor.
   */
  public SnapshotBuilder() {
    this.syncContext = new SyncContext();
    this.snapshot = new Snapshot("Snapshot " + new Date());
    this.podMembersMap = new HashMap<String, PodMemberDTO>();
  }

  /**
   * Add a product for the snapshot.
   * @param product product associated to this snapshot.
   * @return this SnapshotBuilder.
   */  
  public SnapshotBuilder withProduct(Product product) {
    Assert.notNull(product, "product cannot be null");
    Assert.notNull(product.getId(), "product must have an id");
    this.product = product;
    snapshot.setProduct(product);
    return this;
  } 
  
  /**
   * Add Reader to this builder in order to read the data.
   * @param reader the Reader to add.
   * @return a ProjectDataSetBuilder with the Reader added.
   */
  @SuppressWarnings("unchecked")
  public SnapshotBuilder addReader(
      @SuppressWarnings("rawtypes") final Reader reader) {
    Assert.notNull(snapshot.getProduct(), "a product should be first added.");
    reader.readInto(this,syncContext);
    return this;
  }
  

  @Override
  public SnapshotBuilder addMembersMap(Map<String, PodMemberDTO> podMembersMap) {
    this.podMembersMap = podMembersMap;
    return this;
  }

  /**
   * Add a release data of a project.
   * 
   * @param name the name of the release.
   * @param projectId the project Id of the release.
   * @return the same ReleasesBuilder with the release data.
   */
  public SnapshotBuilder addRelease(String name, Long projectId) {
    Project project = projectRepository.findOne(projectId);
    Assert.notNull(project, "project cannot be null");
    Assert.isTrue((project.getProduct().equals(product)),
        "this project is not of the product being processed");
    snapshot.getReleases().add(new Release(name, snapshot, project));
    return this;
  }


  /**
   * Add a sprint data.
   * 
   * @param name the name of the sprint.
   * @param startDate the start date.
   * @param endDate the end date.
   * @param rootTasks a list of DTO with task data prepared to build the domain
   *        task tree associated to this sprint.
   * @return the same ReleasesBuilder with the sprint data.
   */
  public SnapshotBuilder addSprint(String name, Date startDate, Date endDate,
      List<TaskDTO> rootTasks) {
    Assert.notEmpty(snapshot.getReleases(), "releases cannot be  empty");
    Release release = snapshot.getReleases().get(snapshot.getReleases().size() - 1);

    Sprint sprint = new Sprint(name, release, startDate, endDate);
    snapshot.getSprints().add(sprint);  
    snapshot.getTasks().addAll(buildTasksTree(rootTasks, release, sprint));  
    return this;

  }
  
  /**
   * Add a backlog.
   * 
   * @param rootTasks a list of DTO with task data prepared to build the domain
   *        task tree associated to this release.
   * @return the same ReleasesBuilder with the backlog data.
   */
  public SnapshotBuilder addBacklog(List<TaskDTO> rootTasks) {
    Assert.notEmpty(snapshot.getReleases(), "releases cannot be  empty");
    Release release = snapshot.getReleases().get(snapshot.getReleases().size() - 1);
    
    snapshot.getTasks().addAll(buildTasksTree(rootTasks, release, null));
    
    List<Task> taskList = new ArrayList<Task>();
    for (TaskDTO taskDto : rootTasks) {
      addTask(taskDto,   null,  release ,  null, taskList);
    }
    
    return this;

  }
  
  /**
   * Save Snapshot data to the DB with all associated entities.
   * @return the Snapshot.
   */
  public Snapshot build() {
    this.createSprintPodMetrics();
    return snapshot;
  }  

  private void createSprintPodMetrics() {
    for(Pod pod: snapshot.getPods()) {
      for (Sprint sprint: snapshot.getSprints()) {
        SprintPodMetric spm = new SprintPodMetric(sprint, pod);
        List<Task> allTasks = new LinkedList<Task>();
        this.collectTasksFrom(pod, sprint, snapshot.getTasks(), allTasks);
        
        Stream<Task> tasks = allTasks.stream();
        double vel = tasks.filter(t -> t.isAccepted()).mapToDouble(Task::getEffort).sum();
        spm.setAcceptedStoryPoints((int) vel);
        
        snapshot.addSprintMetric(spm);
      }
    }
  }

  private void collectTasksFrom(Pod pod, Sprint sprint, List<Task> rootTasks, List<Task> collect) {
    for(Task task: rootTasks) {
      if(sprint.equals(task.getSprint()) && task.getOwner() != null && task.getOwner().getPod().equals(pod)) {
        collect.add(task);
        Task parent = task.getParentTask();
        //TODO: Refactor Tasks hierarchy 
        //      Parents should have references to children, 
        //      or the builder should keep references to leaf tasks.
        //      it's complicated to naviate the hierarhcy otherwise.
        while(parent != null) {
          if (sprint.equals(parent.getSprint()) && parent.getOwner() != null && parent.getOwner().getPod().equals(pod)) {
            collect.add(parent);
            parent = parent.getParentTask();
          }
        }
      }
    }
  }

  List<Task> buildTasksTree(List<TaskDTO> rootTasks, Release release,
      Sprint sprint) {

    List<Task> taskList = new ArrayList<Task>();
    for (TaskDTO taskDto : rootTasks) {
      addTask(taskDto, null, release, sprint, taskList);
    }
    return taskList;
  }
  
  void addTask(TaskDTO taskDTO, Task parentTask, Release release,
      Sprint sprint, List<Task> taskList) {

    Task task = new Task(release, sprint, parentTask, taskDTO);
    
    String owner = taskDTO.getOwner();
    if (!StringUtils.isEmpty(owner)) {
      PodMemberDTO podMemberDTO = podMembersMap.get(owner);
      if (podMemberDTO != null) {
        task.setOwner(getFromListOrCreateMember(podMemberDTO));
      } else {
        syncContext.warn("Task owner not found: " + owner);
      }
    }
    taskList.add(task);
    for (TaskDTO subTask : taskDTO.getSubTasks()) {
      addTask(subTask, task, release, sprint, taskList);
    }
  }
  
  // Assume a member cannot belong to 2 PODs
  private PodMember getFromListOrCreateMember(PodMemberDTO podMemberDTO) {
    for (PodMember podMember : snapshot.getPodMembers()) {
      if (podMember.getEmail().equals(podMemberDTO.getUserName())) {
        return podMember;
      }
    }
    Pod pod = getFromListOrCreatePod(podMemberDTO.getPodName());
    
    PodMember newPodMember = new PodMember(podMemberDTO.getFirstName(),
        podMemberDTO.getLastName(), podMemberDTO.getUserName(), pod);
    snapshot.getPodMembers().add(newPodMember);
    
    return newPodMember;
  }
  
  
  private Pod getFromListOrCreatePod(String podName) {
    for (Pod pod : snapshot.getPods()) {
      if (pod.getName().equals(podName)) {
        return pod;
      }
    }
    Pod newPod = new Pod(podName);
    snapshot.getPods().add(newPod);
    return newPod;
  }
}
