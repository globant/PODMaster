package com.globant.agilepodmaster.core;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
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
  @OneToMany
//  @Cascade({CascadeType.ALL})
  private Set<Pod> pods = new HashSet<Pod>();
  
  @Getter
  @OneToMany
//  @Cascade({CascadeType.ALL})
  private Set<PodMember> podMembers  = new HashSet<PodMember>();
  
  @Getter
  @OneToMany
  private Set<Organization> organizations = new HashSet<Organization>();
  
  @Getter
  @OneToMany
  private Set<Product> products = new HashSet<Product>();

  @Getter
  @OneToMany
  private Set<Project> projects = new HashSet<Project>();

  @Getter
  @OneToMany
  private Set<Release> releases = new HashSet<Release>();
  
  @Getter
  @OneToMany
  private Set<Sprint> sprints = new HashSet<Sprint>();
  
  @Getter
  @OneToMany
  private Set<Task> tasks = new HashSet<Task>();

  @Getter
  @OneToMany
  private Set<SprintPodMetric> sprintMetrics = new HashSet<SprintPodMetric>();

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

  public void addSprintMetric(SprintPodMetric spm) {
    sprintMetrics.add(spm);
  }

  public void addOrganization(Organization organization) {
    organizations.add(organization);
  }

  public void addProject(Project project) {
    projects.add(project);
  }

  public void addProduct(Product product) {
    products.add(product);
  }
  
  public void addRelease(Release release) {
//    release.setSnapshot(this);
    releases.add(release);
  }

  public void addSprint(Sprint sprint) {
    sprints.add(sprint);
  }

  public void addTask(Task task) {
    tasks.add(task);
  }

  public void addPod(Pod pod) {
    pods.add(pod);
  }

  public void addPodMember(PodMember podMember) {
    podMembers.add(podMember);
  }
}
