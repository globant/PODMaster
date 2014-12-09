package com.globant.agilepodmaster.core;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import org.springframework.util.Assert;

/**
 * Snapshot represents a set of data read from different data sources through
 * the synchronization process. It is associated to a product.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Entity
@EqualsAndHashCode(callSuper = false, of = { "name" })
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Snapshot extends AbstractEntity {

  @NonNull
  private String name;

  @Setter(AccessLevel.NONE)
  @NotNull
  private Date creationDate;
  
  @Getter
  @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "snapshot")
  private Set<Pod> pods = new HashSet<Pod>();
  
  @Getter
  @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "snapshot")
  private Set<PodMember> podMembers  = new HashSet<PodMember>();

  @Getter
  @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "snapshot")
  private Set<Organization> organizations = new HashSet<Organization>();

  @Getter
  @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "snapshot")
  private Set<Product> products = new HashSet<Product>();

  @Getter
  @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "snapshot")
  private Set<Project> projects = new HashSet<Project>();

  @Getter
  @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "snapshot")
  private Set<Release> releases = new HashSet<Release>();
  
  @Getter
  @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "snapshot")
  private Set<Sprint> sprints = new HashSet<Sprint>();
  
  @Getter
  @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "snapshot")
  private Set<Task> tasks = new HashSet<Task>();

  @Getter
  @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinColumn(name = "snapshot_id", insertable = false, updatable = false)
  private Set<SprintPodMetric> sprintPodMetrics = new HashSet<SprintPodMetric>();

  @Getter
  @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinColumn(name = "snapshot_id", insertable = false, updatable = false)
  private Set<ProjectPodMetric> projectPodMetrics = new HashSet<ProjectPodMetric>();
  
  @Getter
  @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinColumn(name = "snapshot_id", insertable = false, updatable = false)
  private Set<ProjectMetric> projectMetrics = new HashSet<ProjectMetric>();

  /**
   * Constructor.
   * 
   * @param name the name of the snapshot.
   */
  public Snapshot(String name) {
    super();
  }

  /**
   * creationDate getter.
   * 
   * @return the date.
   */
  public Date getCreationDate() {
    return new Date(creationDate.getTime());
  }

  /**
   * creationDate setter.
   * 
   * @param creationDate the date.
   */
  public void setCreationDate(Date creationDate) {
    Assert.notNull(creationDate, "creationDate is null");
    this.creationDate = new Date(creationDate.getTime());
  }
  
  /**
   * Add a SprintPodMetrics to this snapshot.
   * @param spms the SprintPodMetrics.
   */
  public void addSprintMetrics(List<SprintPodMetric> spms) {
    spms.forEach(t -> t.setSnapshot(this));
    sprintPodMetrics.addAll(spms);
  }


  /**
   * Add an organization to this snapshot.
   * @param organization the organization.
   */
  public void addOrganization(Organization organization) {
    organization.setSnapshot(this);
    organizations.add(organization);
  }

  /**
   * Add a project to this snapshot.
   * @param project the project.
   */
  public void addProject(Project project) {
    project.setSnapshot(this);
    projects.add(project);
  }

  /**
   * Add a product to this snapshot.
   * @param product the product.
   */
  public void addProduct(Product product) {
    product.setSnapshot(this);
    products.add(product);
  }
  
  /**
   * Add a release to this snapshot.
   * @param release the release.
   */
  public void addRelease(Release release) {
    release.setSnapshot(this);
    releases.add(release);
  }

  /**
   * Add a sprint to this snapshot.
   * @param sprint the sprint.
   */
  public void addSprint(Sprint sprint) {
    sprint.setSnapshot(this);
    sprints.add(sprint);
  }

  /**
   * Add a task to this snapshot.
   * @param task the task.
   */
  public void addTask(Task task) {
    task.setSnapshot(this);
    tasks.add(task);
  }

  /**
   * Add a pod to this snapshot.
   * @param pod the pod.
   */
  public void addPod(Pod pod) {
    pod.setSnapshot(this);
    pods.add(pod);
  }

  /**
   * Add a pod member to this snapshot.
   * @param podMember the pod member.
   */
  public void addPodMember(PodMember podMember) {
    podMember.setSnapshot(this);
    podMembers.add(podMember);
  }
  
  /**
   * Add a ProjectMetrics to this snapshot.
   * @param pms the ProjectMetrics.
   */
  public void addProjectMetrics(List<ProjectMetric> pms) {
    pms.forEach(t -> t.setSnapshot(this));
    projectMetrics.addAll(pms);
  }

}
