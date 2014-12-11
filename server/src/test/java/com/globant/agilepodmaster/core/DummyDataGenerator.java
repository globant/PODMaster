package com.globant.agilepodmaster.core;

import com.globant.agilepodmaster.sync.SnapshotBuilder;
import com.globant.agilepodmaster.sync.SyncContext;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Generator of snapshots representing different scenarios.
 * @author Andres Postiglioni.
 *
 */
public class DummyDataGenerator {

  /**
   * Build snapshot with scenario1.
   * @return an snapshot with scenario1.
   */
  public Snapshot buildScenario1() {
    return this.populateScenario1(new SnapshotBuilder(new SyncContext())).build();
  }

  /**
   * Build snapshot with scenario2.
   * @return an snapshot with scenario2.
   */
  public Snapshot buildScenario2() {
    return this.populateScenario2(new SnapshotBuilder(new SyncContext())).build();
  }
  
  /**
   * Build snapshot with scenario3.
   * @return an snapshot with scenario3.
   */
  public Snapshot buildScenario3() {
    return this.populateScenario3(new SnapshotBuilder(new SyncContext())).build();
  }
  
  /**
   * Scenario 2.
   * - 2 pods
   * - each pod has assigned 2 tasks of 10 SP each
   * - the tasks are not included in any sprint
   *  
   * @param builder builder to create snapshot.
   * @return the same builder, populated with data representing the scenario described
   */
  public SnapshotBuilder populateScenario2(SnapshotBuilder builder) {
    String[] pod1Members = { "scenario1-pod1-member1", "scenario1-pod1-member2" };
    String[] pod2Members = { "scenario1-pod2-member1", "scenario1-pod2-member2" };

    builder
    .withOrganization("scenario2-organization")
      .addProduct("scenario2-product 1")
        .addProject("scenario2-project 1", 100)
          .withRelease("scenario2-release 1")
            .withSprint("scenario1-sprint-q1-1", toDate(2014, 01, 1), toDate(2014, 01, 14))
              .withTask().name("task1").effort(5f).owner(pod1Members[0]).addToSprint()
              .withTask().name("task2").effort(5f).owner(pod1Members[1]).addToSprint()
              .withTask().name("task1").effort(5f).owner(pod2Members[0]).addToSprint()
              .withTask().name("task2").effort(5f).owner(pod2Members[1]).addToSprint()
            .addToRelease()
          .addToProject()
        .addToProduct()
      .addToOrganization()
      .addToSnapshot()
      // Add pods
      .withPod("scenario2-pod1")
      .withPodMember("pepe", "pepe", pod1Members[0]).addToPod()
      .withPodMember("pepe", "pepe", pod1Members[1]).addToPod()
      .addToSnapshot()
      .withPod("scenario2-pod2")
      .withPodMember("pepe", "pepe", pod2Members[0]).addToPod()
      .withPodMember("pepe", "pepe", pod2Members[1]).addToPod()
      .addToSnapshot();

    return builder;
  }
  
  
  /**
   * Scenario 1.
   * - Three pods with two members each
   * - One organization with one product and one project
   * - 8 prints (2 per quarter)
   * - For all pods, the tasks status is CLOSE for all sprint tasks:
   *   - Q1 
   *     - Sprint 1: 30 SP
   *     - Sprint 2: 30 SP
   *   - Q2 
   *     - Sprint 1: 60 SP
   *     - Sprint 2: 60 SP
   *   - Q3 
   *     - Sprint 1: 150 SP
   *     - Sprint 2: 150 SP
   *   - Q4 
   *     - Sprint 1: 300 SP
   *     - Sprint 2: 300 SP
   *
   * @param builder builder to create the snapshot.
   * @return the same builder, populated with data representing the scenario described
   */  
  @SuppressWarnings("PMD")
  public SnapshotBuilder populateScenario1(SnapshotBuilder builder) {
    String[] pod1Members = { "scenario1-pod1-member1", "scenario1-pod1-member2" };
    String[] pod2Members = { "scenario1-pod2-member1", "scenario1-pod2-member2" };
    String[] pod3Members = { "scenario1-pod3-member1", "scenario1-pod3-member2" };

    builder
    .withOrganization("scenario1-organization")
      .addProduct("scenario1-product 1")
        .addProject("scenario1-project 1", 1512)
          .withRelease("scenario1-release 1")
            .withSprint("scenario1-sprint-q1-1", toDate(2014, 01, 1), toDate(2014, 01, 14))
              .withTask().name("task1").effort(5f).owner(pod1Members[0]).addToSprint()
              .withTask().name("task2").effort(5f).owner(pod1Members[1]).addToSprint()
              .withTask().name("task1").effort(5f).owner(pod2Members[0]).addToSprint()
              .withTask().name("task2").effort(5f).owner(pod2Members[1]).addToSprint()
              .withTask().name("task1").effort(5f).owner(pod3Members[0]).addToSprint()
              .withTask().name("task2").effort(5f).owner(pod3Members[1]).addToSprint()
            .addToRelease()
            .withSprint("scenario1-sprint-q1-2", toDate(2014, 01, 1), toDate(2014, 01, 14))
              .withTask().name("task1").effort(5f).owner(pod1Members[0]).addToSprint()
              .withTask().name("task2").effort(5f).owner(pod1Members[1]).addToSprint()
              .withTask().name("task1").effort(5f).owner(pod2Members[0]).addToSprint()
              .withTask().name("task2").effort(5f).owner(pod2Members[1]).addToSprint()
              .withTask().name("task1").effort(5f).owner(pod3Members[0]).addToSprint()
              .withTask().name("task2").effort(5f).owner(pod3Members[1]).addToSprint()
            .addToRelease()
            .withSprint("scenario1-sprint-q2-1", toDate(2014, 04, 1), toDate(2014, 04, 14))
              .withTask().name("task1").effort(10f).owner(pod1Members[0]).addToSprint()
              .withTask().name("task2").effort(10f).owner(pod1Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod2Members[0]).addToSprint()
              .withTask().name("task2").effort(10f).owner(pod2Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod3Members[0]).addToSprint()
              .withTask().name("task2").effort(10f).owner(pod3Members[1]).addToSprint()
            .addToRelease()
            .withSprint("scenario1-sprint-q2-2", toDate(2014, 04, 1), toDate(2014, 04, 14))
              .withTask().name("task1").effort(10f).owner(pod1Members[0]).addToSprint()
              .withTask().name("task2").effort(10f).owner(pod1Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod2Members[0]).addToSprint()
              .withTask().name("task2").effort(10f).owner(pod2Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod3Members[0]).addToSprint()
              .withTask().name("task2").effort(10f).owner(pod3Members[1]).addToSprint()
            .addToRelease()
            .withSprint("scenario1-sprint-q3-1", toDate(2014, 07, 1), toDate(2014, 07, 14))
              .withTask().name("task1").effort(10f).owner(pod1Members[0]).addToSprint()
              .withTask().name("task2").effort(40f).owner(pod1Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod2Members[0]).addToSprint()
              .withTask().name("task2").effort(40f).owner(pod2Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod3Members[0]).addToSprint()
              .withTask().name("task2").effort(40f).owner(pod3Members[1]).addToSprint()
            .addToRelease()
            .withSprint("scenario1-sprint-q3-2", toDate(2014, 07, 1), toDate(2014, 07, 14))
              .withTask().name("task1").effort(10f).owner(pod1Members[0]).addToSprint()
              .withTask().name("task2").effort(40f).owner(pod1Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod2Members[0]).addToSprint()
              .withTask().name("task2").effort(40f).owner(pod2Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod3Members[0]).addToSprint()
              .withTask().name("task2").effort(40f).owner(pod3Members[1]).addToSprint()
            .addToRelease()
            .withSprint("scenario1-sprint-q4-1", toDate(2014, 10, 1), toDate(2014, 10, 14))
              .withTask().name("task1").effort(10f).owner(pod1Members[0]).addToSprint()
              .withTask().name("task2").effort(90f).owner(pod1Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod2Members[0]).addToSprint()
              .withTask().name("task2").effort(90f).owner(pod2Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod3Members[0]).addToSprint()
              .withTask().name("task2").effort(90f).owner(pod3Members[1]).addToSprint()
            .addToRelease()
            .withSprint("scenario1-sprint-q4-2", toDate(2014, 10, 1), toDate(2014, 10, 14))
              .withTask().name("task1").effort(10f).owner(pod1Members[0]).addToSprint()
              .withTask().name("task2").effort(90f).owner(pod1Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod2Members[0]).addToSprint()
              .withTask().name("task2").effort(90f).owner(pod2Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod3Members[0]).addToSprint()
              .withTask().name("task2").effort(90f).owner(pod3Members[1]).addToSprint()
            .addToRelease()
          .addToProject()
        .addToProduct()
      .addToOrganization()
      .addToSnapshot()

      // Add pods
      .withPod("scenario1-pod1")
      .withPodMember("pepe", "pepe", pod1Members[0]).addToPod()
      .withPodMember("pepe", "pepe", pod1Members[1]).addToPod()
      .addToSnapshot()
      .withPod("scenario1-pod2")
      .withPodMember("pepe", "pepe", pod2Members[0]).addToPod()
      .withPodMember("pepe", "pepe", pod2Members[1]).addToPod()
      .addToSnapshot()
      .withPod("scenario1-pod3")
      .withPodMember("pepe", "pepe", pod3Members[0]).addToPod()
      .withPodMember("pepe", "pepe", pod3Members[1]).addToPod()
      .addToSnapshot();
    
    return builder;
  }
  
  
  /**
   * Scenario 3.
   * - 2 pods
   * - 2 sprints
   * - Sprint 1 : POD1 -> 1 bug, POD2 -> 4 bugs
   * - Sprint 2 : POD1 -> 2 bugs, POD2 -> 3 bugs
   *  
   * @param builder the builder to create the snapshot.
   * @return the same builder, populated with data representing the scenario described
   */
  public SnapshotBuilder populateScenario3(SnapshotBuilder builder) {
    String[] pod1Members = { "scenario3-pod1-member1", "scenario3-pod1-member2" };
    String[] pod2Members = { "scenario3-pod2-member1", "scenario3-pod2-member2" };

    builder
    .withOrganization("scenario3-organization")
      .addProduct("scenario3-product 1")
        .addProject("scenario3-project 1", 500)
          .withRelease("scenario3-release 1")
            .withSprint("scenario3-sprint-1", toDate(2014, 01, 1), toDate(2014, 01, 14))
              .withTask().name("bug1").type("bug").owner(pod1Members[0]).addToSprint()
              .withTask().name("bug2").type("bug").owner(pod2Members[0]).addToSprint()
              .withTask().name("bug3").type("bug").owner(pod2Members[0]).addToSprint()
              .withTask().name("bug4").type("bug").owner(pod2Members[1]).addToSprint()
            .addToRelease()
            .withSprint("scenario3-sprint-2", toDate(2014, 01, 1), toDate(2014, 01, 14))
              .withTask().name("bug5").type("bug").owner(pod1Members[0]).addToSprint()
              .withTask().name("bug6").type("bug").owner(pod1Members[1]).addToSprint()
              .withTask().name("bug7").type("bug").owner(pod2Members[0]).addToSprint()
              .withTask().name("bug8").type("bug").owner(pod2Members[1]).addToSprint()
              .withTask().name("bug9").type("bug").owner(pod2Members[0]).addToSprint()
              .withTask().name("bug10").type("bug").owner(pod2Members[1]).addToSprint() 
            .addToRelease()            
          .addToProject()
        .addToProduct()
      .addToOrganization()
      .addToSnapshot()
      // Add pods
      .withPod("scenario3-pod1")
      .withPodMember("pepe", "pepe", pod1Members[0]).addToPod()
      .withPodMember("pepe", "pepe", pod1Members[1]).addToPod()
      .addToSnapshot()
      .withPod("scenario3-pod2")
      .withPodMember("pepe", "pepe", pod2Members[0]).addToPod()
      .withPodMember("pepe", "pepe", pod2Members[1]).addToPod()
      .addToSnapshot();
    

    return builder;
  }
  
  private Date toDate(int year, int month, int day) {
    LocalDate localDate = LocalDate.of(year, month, day);
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }
}
