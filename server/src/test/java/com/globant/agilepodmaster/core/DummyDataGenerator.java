package com.globant.agilepodmaster.core;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.globant.agilepodmaster.sync.SnapshotBuilder;
import com.globant.agilepodmaster.sync.SyncContext;

public class DummyDataGenerator {

  public Snapshot buildScenario1() {
    return this.populateScenario1(new SnapshotBuilder(new SyncContext())).build();
  }

  public Snapshot buildScenario2() {
    return this.populateScenario2(new SnapshotBuilder(new SyncContext())).build();
  }
  
  /**
   * Scenario 2
   * - 2 pods
   * - each pod has assigned 2 tasks of 10 SP each
   * - the tasks are not included in any sprint
   *  
   * @param builder
   * @return the same builder, populated with data representing the scenario described
   */
  public SnapshotBuilder populateScenario2(SnapshotBuilder builder) {
    String[] pod1Members = { "scenario1-pod1-member1", "scenario1-pod1-member2" };
    String[] pod2Members = { "scenario1-pod2-member1", "scenario1-pod2-member2" };

    builder
    .withOrganization("scenario2-organization")
      .addProduct("scenario2-product 1")
        .addProject("scenario2-project 1")
          .withRelease("scenario2-release 1")
            .withBacklog()
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
   * Scenario 1
   * - Three pods with two members each
   * - One organization with one product and one project
   * - 8 prints (2 per quarter)
   * - For all pods, the tasks status is:
   *   - Q1 
   *     - Sprint 1: 10 SP closed out of 10
   *     - Sprint 2: 10 SP closed out of 10
   *   - Q2 
   *     - Sprint 1: 10 SP closed out of 20
   *     - Sprint 2: 10 SP closed out of 20
   *   - Q3 
   *     - Sprint 1: 10 SP closed out of 50
   *     - Sprint 2: 10 SP closed out of 50
   *   - Q4 
   *     - Sprint 1: 10 SP closed out of 100
   *     - Sprint 2: 10 SP closed out of 100
   *
   * @param builder
   * @return the same builder, populated with data representing the scenario described
   */  
  public SnapshotBuilder populateScenario1(SnapshotBuilder builder) {
    String[] pod1Members = { "scenario1-pod1-member1", "scenario1-pod1-member2" };
    String[] pod2Members = { "scenario1-pod2-member1", "scenario1-pod2-member2" };
    String[] pod3Members = { "scenario1-pod3-member1", "scenario1-pod3-member2" };

    builder
    .withOrganization("scenario1-organization")
      .addProduct("scenario1-product 1")
        .addProject("scenario1-project 1")
          .withRelease("scenario1-release 1")
            .withSprint("scenario1-sprint-q1-1", toDate(2014, 01, 1), toDate(2014, 01, 14))
              .withTask().name("task1").effort(5f).owner(pod1Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(5f).owner(pod1Members[1]).status("CLOSED").addToSprint()
              .withTask().name("task1").effort(5f).owner(pod2Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(5f).owner(pod2Members[1]).status("CLOSED").addToSprint()
              .withTask().name("task1").effort(5f).owner(pod3Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(5f).owner(pod3Members[1]).status("CLOSED").addToSprint()
            .addToRelease()
            .withSprint("scenario1-sprint-q1-2", toDate(2014, 01, 1), toDate(2014, 01, 14))
              .withTask().name("task1").effort(5f).owner(pod1Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(5f).owner(pod1Members[1]).status("CLOSED").addToSprint()
              .withTask().name("task1").effort(5f).owner(pod2Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(5f).owner(pod2Members[1]).status("CLOSED").addToSprint()
              .withTask().name("task1").effort(5f).owner(pod3Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(5f).owner(pod3Members[1]).status("CLOSED").addToSprint()
            .addToRelease()
            .withSprint("scenario1-sprint-q2-1", toDate(2014, 04, 1), toDate(2014, 04, 14))
              .withTask().name("task1").effort(10f).owner(pod1Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(10f).owner(pod1Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod2Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(10f).owner(pod2Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod3Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(10f).owner(pod3Members[1]).addToSprint()
            .addToRelease()
            .withSprint("scenario1-sprint-q2-2", toDate(2014, 04, 1), toDate(2014, 04, 14))
              .withTask().name("task1").effort(10f).owner(pod1Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(10f).owner(pod1Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod2Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(10f).owner(pod2Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod3Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(10f).owner(pod3Members[1]).addToSprint()
            .addToRelease()
            .withSprint("scenario1-sprint-q3-1", toDate(2014, 07, 1), toDate(2014, 07, 14))
              .withTask().name("task1").effort(10f).owner(pod1Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(40f).owner(pod1Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod2Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(40f).owner(pod2Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod3Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(40f).owner(pod3Members[1]).addToSprint()
            .addToRelease()
            .withSprint("scenario1-sprint-q3-2", toDate(2014, 07, 1), toDate(2014, 07, 14))
              .withTask().name("task1").effort(10f).owner(pod1Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(40f).owner(pod1Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod2Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(40f).owner(pod2Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod3Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(40f).owner(pod3Members[1]).addToSprint()
            .addToRelease()
            .withSprint("scenario1-sprint-q4-1", toDate(2014, 10, 1), toDate(2014, 10, 14))
              .withTask().name("task1").effort(10f).owner(pod1Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(90f).owner(pod1Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod2Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(90f).owner(pod2Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod3Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(90f).owner(pod3Members[1]).addToSprint()
            .addToRelease()
            .withSprint("scenario1-sprint-q4-2", toDate(2014, 10, 1), toDate(2014, 10, 14))
              .withTask().name("task1").effort(10f).owner(pod1Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(90f).owner(pod1Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod2Members[0]).status("CLOSED").addToSprint()
              .withTask().name("task2").effort(90f).owner(pod2Members[1]).addToSprint()
              .withTask().name("task1").effort(10f).owner(pod3Members[0]).status("CLOSED").addToSprint()
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
  
  private Date toDate(int year, int month, int day) {
    LocalDate localDate = LocalDate.of(year, month, day);
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }
}
