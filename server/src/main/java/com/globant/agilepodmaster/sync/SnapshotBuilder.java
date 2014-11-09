package com.globant.agilepodmaster.sync;

import com.globant.agilepodmaster.core.Pod;
import com.globant.agilepodmaster.core.PodMember;
import com.globant.agilepodmaster.core.PodMemberRepository;
import com.globant.agilepodmaster.core.PodRepository;
import com.globant.agilepodmaster.core.Product;
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
import com.globant.agilepodmaster.sync.reading.PodsBuilder;
import com.globant.agilepodmaster.sync.reading.Reader;
import com.globant.agilepodmaster.sync.reading.ReleasesBuilder;
import com.globant.agilepodmaster.sync.reading.TaskDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

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

  @Autowired
  private SnapshotRepository snapshotRepository;

  @Autowired
  private ReleaseRepository releaseRepository;

  @Autowired
  private SprintRepository sprintRepository;

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private PodRepository podRepository;

  @Autowired
  private PodMemberRepository podMemberRepository;
    
  private Snapshot snapshot; 
  
  private Product product; 

  private List<Release> releases;
  
  private List<Sprint> sprints;
  
  private List<Task> tasks;
  
  private List<Pod> pods;
  
  private List<PodMember> podMembers;
  
  private Map<String, PodMemberDTO> podMembersMap;
  
  @Getter
  private SyncContext syncContext;

  /**
   * Constructor.
   */
  public SnapshotBuilder() {
    this.syncContext = new SyncContext();
    this.snapshot = new Snapshot("Snapshot " + new Date());
    this.pods = new ArrayList<Pod>();
    this.podMembers = new ArrayList<PodMember>();
    this.releases = new ArrayList<Release>(); 
    this.sprints = new ArrayList<Sprint>();
    this.tasks = new ArrayList<Task>();
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
    releases.add(new Release(name, snapshot, project));
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
    Assert.notEmpty(releases, "releases cannot be  empty");
    Release release = releases.get(releases.size() - 1);

    Sprint sprint = new Sprint(name, release, startDate, endDate);
    sprints.add(sprint);  
    tasks.addAll(buildTasksTree(rootTasks, release, sprint));  
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
    Assert.notEmpty(releases, "releases cannot be  empty");
    Release release = releases.get(releases.size() - 1);
    
    tasks.addAll(buildTasksTree(rootTasks, release, null));
    
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
    Snapshot result = snapshotRepository.save(snapshot);
    releaseRepository.save(releases);
    sprintRepository.save(sprints);
    podRepository.save(pods);
    podMemberRepository.save(podMembers);
    taskRepository.save(tasks);
    return result;
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
        task.setOwner(getFromListOrCreateMember(podMemberDTO, sprint));
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
  private PodMember getFromListOrCreateMember(PodMemberDTO podMemberDTO,
      Sprint sprint) {
    for (PodMember podMember : podMembers) {
      if (podMember.getEmail().equals(podMemberDTO.getUserName())) {
        return podMember;
      }
    }
    Pod pod = getFromListOrCreatePod(podMemberDTO.getPodName(), sprint);
    
    PodMember newPodMember = new PodMember(podMemberDTO.getFirstName(),
        podMemberDTO.getLastName(), podMemberDTO.getUserName(), pod);
    podMembers.add(newPodMember);
    
    return newPodMember;
  }
  
  
  private Pod getFromListOrCreatePod(String podName, Sprint sprint) {
    for (Pod pod : pods) {
      if (pod.getName().equals(podName)) {
        return pod;
      }
    }
    Pod newPod = new Pod(podName, sprint);
    pods.add(newPod);
    return newPod;
  }

}