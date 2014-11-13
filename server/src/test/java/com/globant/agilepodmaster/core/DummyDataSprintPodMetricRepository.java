package com.globant.agilepodmaster.core;

import static com.mysema.query.collections.CollQueryFactory.from;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;

@SuppressWarnings("PMD.TooManyMethods")
public class DummyDataSprintPodMetricRepository implements SprintPodMetricRepository {
  private List<SprintPodMetric> list = new LinkedList<SprintPodMetric>();

  public DummyDataSprintPodMetricRepository() {
    List<Pod> pods = Arrays.asList(
        new Pod("pod1"),
        new Pod("pod2"),
        new Pod("pod3")
    );

    List<Sprint> sprints = Arrays.asList(
        newSprint(2014, 01, 1, "sprint-q1-1", null),
        newSprint(2014, 01, 1, "sprint-q1-2", null),
        newSprint(2014, 04, 1, "sprint-q2-1", null),
        newSprint(2014, 04, 1, "sprint-q2-2", null),
        newSprint(2014, 07, 1, "sprint-q3-1", null),
        newSprint(2014, 07, 1, "sprint-q3-2", null),
        newSprint(2014, 10, 1, "sprint-q4-1", null),
        newSprint(2014, 10, 1, "sprint-q4-2", null)
    );
    
    sprints.forEach(s -> pods.forEach(p -> list.add(newSprintPodMetric(s, p))));
  }

  private SprintPodMetric newSprintPodMetric(Sprint sprint, Pod pod) {
    SprintPodMetric spm = new SprintPodMetric(sprint, pod);
    spm.setAcceptedStoryPoints(10);
    
    return spm;
  }

  private Sprint newSprint(int year, int month, int day, String name, Release release) {
    LocalDate localDate = LocalDate.of(year, month, day);
    return new Sprint(name, release, toDate(localDate), toDate(localDate.plusDays(14)));
  }

  private Date toDate(LocalDate localDate) {
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  @Override
  public SprintPodMetric findOne(Predicate predicate) {
    return this.findAll(predicate).iterator().next();
  }

  @Override
  public Iterable<SprintPodMetric> findAll(Predicate predicate) {
    QSprintPodMetric spm = QSprintPodMetric.sprintPodMetric;
    return from(spm, list).where(predicate).list(spm);
  }
  
  @Override
  public Iterable<SprintPodMetric> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Page<SprintPodMetric> findAll(Predicate predicate, Pageable pageable) {
    throw new UnsupportedOperationException();
  }

  @Override
  public long count(Predicate predicate) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends SprintPodMetric> S save(S entity) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends SprintPodMetric> Iterable<S> save(Iterable<S> entities) {
    throw new UnsupportedOperationException();
  }

  @Override
  public SprintPodMetric findOne(Long id) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean exists(Long id) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Iterable<SprintPodMetric> findAll() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Iterable<SprintPodMetric> findAll(Iterable<Long> ids) {
    throw new UnsupportedOperationException();
  }

  @Override
  public long count() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void delete(Long id) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void delete(SprintPodMetric entity) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void delete(Iterable<? extends SprintPodMetric> entities) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteAll() {
    throw new UnsupportedOperationException();
  }  
}
