package com.globant.agilepodmaster.metrics;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import com.globant.agilepodmaster.core.AbstractMetricRepository;
import com.globant.agilepodmaster.core.CollectionBasedQueryDslPredicateExecutor;
import com.globant.agilepodmaster.core.DummyDataGenerator;
import com.globant.agilepodmaster.core.ProjectPodMetric;
import com.globant.agilepodmaster.core.QProjectPodMetric;
import com.globant.agilepodmaster.core.QSprintPodMetric;
import com.globant.agilepodmaster.core.Quarter;
import com.globant.agilepodmaster.core.SprintPodMetric;
import com.globant.agilepodmaster.metrics.partition.Partition;
import com.globant.agilepodmaster.metrics.partition.Partitioner;
import com.globant.agilepodmaster.metrics.partition.PodPartitioner;
import com.globant.agilepodmaster.metrics.partition.QuarterPartitioner;
import com.globant.agilepodmaster.metrics.partition.SprintPartitioner;
import com.globant.agilepodmaster.metrics.partition.YearPartitioner;
import com.mysema.query.types.path.EntityPathBase;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;


/**
 * Test for MetricsAggregatorCommand.
 * 
 * @author Andres Postiglioni.
 *
 */
@RunWith(Parameterized.class)
@SuppressWarnings("PMD")
public class MetricsAggregatorCommandTest {
  private Set<MetricAggregation> expectedResult;
  private List<Partitioner<? extends Partition<?>>> partitioners;
  private Object repo;

  /**
   * Constructor.
   * 
   * @param partitioners input partitioners.
   * @param expectedResult expected results.
   * @param repo repository to be used.
   */
  public MetricsAggregatorCommandTest(
      List<Partitioner<? extends Partition<?>>> partitioners,
      Set<MetricAggregation> expectedResult, Object repo) {
    this.expectedResult = expectedResult;
    this.partitioners = partitioners;
    this.repo = repo;
  }

  /**
   * Testing with different parameters.
   */
  @Test
  @Ignore
  public void test() {
    MetricsAggregatorCommand command = new MetricsAggregatorCommand(
        new MetricsAggregator(), createDummyRepo(repo,
            AbstractMetricRepository.class));

    List<MetricAggregation> aggregations = command.execute(partitioners, null);

    System.out.println("---");
    aggregations.forEach(it -> System.out.println(it));

    assertThat(aggregations, equalTo(expectedResult));

  }

  @SuppressWarnings("unchecked")
  private <T> T createDummyRepo(Object delegate, Class<T> clazz) {
    InvocationHandler handler = new InvocationHandler() {
      @Override
      public Object invoke(Object proxy, Method method, Object[] args)
          throws Throwable {
        // TODO: More accurate filter
        Method toInvoke = Stream
            .of(delegate.getClass().getDeclaredMethods())
            .filter(
                m -> m.getName() == method.getName()
                    && m.getParameterCount() == args.length).findFirst().get();
        return toInvoke.invoke(delegate, args);
      }
    };
    return ((T) Proxy.newProxyInstance(clazz.getClassLoader(),
        new Class[] {clazz}, handler));
  }

  private static <T> CollectionBasedQueryDslPredicateExecutor<T> createRepoMixin(
      EntityPathBase<T> query, Iterable<T> data) {
    return new CollectionBasedQueryDslPredicateExecutor<T>(query, data);
  }

  /**
   * Parameters definition.
   * 
   * @return parameters for test.
   */
  @Parameters
  public static Collection<Object[]> testData() {
    return Arrays.asList(new Object[][] {
        // SprintPodMetric scenarios
        scenarioEmptyPartitionersList(), scenarioYearQuarterPod(),
        scenarioSprintPod(), scenarioSprintPodBugs(), scenarioPod(),

        // ProjectMetric scenarios
        projectMetricsScenarioEmptyPartitionersList(),
        projectMetricsScenarioYearQuarter(), projectMetricsScenarioPod()});
  }

  private static Object[] projectMetricsScenarioYearQuarter() {
    Metric<Integer> remaining = new Metric<Integer>("remaining-story-points",
        20, "story points");


    Set<MetricAggregation> expected = asSet(new MetricAggregation(
        new HashSet<Partition<?>>(), asSet(remaining)));

    List<Partitioner<? extends Partition<?>>> input = Arrays.asList(
        new YearPartitioner(), new QuarterPartitioner());

    Set<ProjectPodMetric> projectMetrics = new DummyDataGenerator()
        .buildScenario2().getProjectMetrics();
    CollectionBasedQueryDslPredicateExecutor<ProjectPodMetric> dataGenerator = createRepoMixin(
        QProjectPodMetric.projectPodMetric, projectMetrics);

    return new Object[] {input, expected, dataGenerator};
  }

  private static Object[] projectMetricsScenarioEmptyPartitionersList() {
    Metric<Integer> remaining = new Metric<Integer>("remaining-story-points",
        20, "story points");


    Set<MetricAggregation> expected = asSet(new MetricAggregation(
        new HashSet<Partition<?>>(), asSet(remaining)));

    List<Partitioner<? extends Partition<?>>> input = 
        new ArrayList<Partitioner<? extends Partition<?>>>();

    Set<ProjectPodMetric> projectMetrics = new DummyDataGenerator()
        .buildScenario2().getProjectMetrics();
    CollectionBasedQueryDslPredicateExecutor<ProjectPodMetric> dataGenerator = createRepoMixin(
        QProjectPodMetric.projectPodMetric, projectMetrics);

    return new Object[] {input, expected, dataGenerator};
  }

  private static Object[] projectMetricsScenarioPod() {
    Partition<String> partitionPod1 = new Partition<String>("pod",
        "scenario2-pod1");
    Partition<String> partitionPod2 = new Partition<String>("pod",
        "scenario2-pod2");

    Metric<Integer> remaining = new Metric<Integer>("remaining-story-points",
        10, "story points");

    Set<MetricAggregation> expected = asSet(new MetricAggregation(
        asSet(partitionPod1), asSet(remaining)), new MetricAggregation(
        asSet(partitionPod2), asSet(remaining)));

    List<Partitioner<? extends Partition<?>>> input = Arrays
        .asList(new PodPartitioner());

    Set<ProjectPodMetric> projectMetrics = new DummyDataGenerator()
        .buildScenario2().getProjectMetrics();
    CollectionBasedQueryDslPredicateExecutor<ProjectPodMetric> dataGenerator = createRepoMixin(
        QProjectPodMetric.projectPodMetric, projectMetrics);

    return new Object[] {input, expected, dataGenerator};
  }

  private static Object[] scenarioEmptyPartitionersList() {
    Metric<Integer> velocity240sp = new Metric<Integer>("velocity", 240,
        "story points");
    Metric<Double> acc = new Metric<Double>("accuracy-of-estimations", 0.45,
        "percentage");
    Metric<Integer> ceroBugs = new Metric<Integer>("bugs", 0, "number of bugs");

    Set<MetricAggregation> expected = asSet(new MetricAggregation(
        new HashSet<Partition<?>>(), asSet(velocity240sp, acc, ceroBugs)));

    List<Partitioner<? extends Partition<?>>> input = 
        new ArrayList<Partitioner<? extends Partition<?>>>();

    CollectionBasedQueryDslPredicateExecutor<SprintPodMetric> dataGenerator = createRepoMixin(
        QSprintPodMetric.sprintPodMetric, new DummyDataGenerator()
            .buildScenario1().getSprintMetrics());

    return new Object[] {input, expected, dataGenerator};
  }

  private static Object[] scenarioPod() {
    Partition<String> partitionPod1 = new Partition<String>("pod",
        "scenario1-pod1");
    Partition<String> partitionPod2 = new Partition<String>("pod",
        "scenario1-pod2");
    Partition<String> partitionPod3 = new Partition<String>("pod",
        "scenario1-pod3");

    Metric<Integer> velocity80sp = new Metric<Integer>("velocity", 80,
        "story points");
    Metric<Double> acc = new Metric<Double>("accuracy-of-estimations", 0.45,
        "percentage");
    Metric<Integer> ceroBugs = new Metric<Integer>("bugs", 0, "number of bugs");

    Set<MetricAggregation> expected = asSet(
        new MetricAggregation(asSet(partitionPod1), asSet(velocity80sp, acc,
            ceroBugs)),
        new MetricAggregation(asSet(partitionPod2), asSet(velocity80sp, acc,
            ceroBugs)),
        new MetricAggregation(asSet(partitionPod3), asSet(velocity80sp, acc,
            ceroBugs)));


    List<Partitioner<? extends Partition<?>>> input = Arrays
        .asList(new PodPartitioner());

    CollectionBasedQueryDslPredicateExecutor<SprintPodMetric> dataGenerator = createRepoMixin(
        QSprintPodMetric.sprintPodMetric, new DummyDataGenerator()
            .buildScenario1().getSprintMetrics());

    return new Object[] {input, expected, dataGenerator};
  }

  private static Object[] scenarioYearQuarterPod() {
    Partition<Quarter> partitionQ1 = new Partition<Quarter>("quarter",
        Quarter.Q1);
    Partition<Quarter> partitionQ2 = new Partition<Quarter>("quarter",
        Quarter.Q2);
    Partition<Quarter> partitionQ3 = new Partition<Quarter>("quarter",
        Quarter.Q3);
    Partition<Quarter> partitionQ4 = new Partition<Quarter>("quarter",
        Quarter.Q4);

    Partition<String> partitionPod1 = new Partition<String>("pod",
        "scenario1-pod1");
    Partition<String> partitionPod2 = new Partition<String>("pod",
        "scenario1-pod2");
    Partition<String> partitionPod3 = new Partition<String>("pod",
        "scenario1-pod3");

    Partition<Integer> partition2014 = new Partition<Integer>("year", 2014);

    Metric<Integer> vel20sp = new Metric<Integer>("velocity", 20,
        "story points");

    Metric<Double> acc01 = new Metric<Double>("accuracy-of-estimations", 0.1,
        "percentage");
    Metric<Double> acc02 = new Metric<Double>("accuracy-of-estimations", 0.2,
        "percentage");
    Metric<Double> acc05 = new Metric<Double>("accuracy-of-estimations", 0.5,
        "percentage");
    Metric<Double> acc1 = new Metric<Double>("accuracy-of-estimations", 1.0,
        "percentage");

    Metric<Integer> ceroBugs = new Metric<Integer>("bugs", 0, "number of bugs");

    Set<Metric<?>> q1Metrics = asSet(vel20sp, acc1, ceroBugs);
    Set<Metric<?>> q2Metrics = asSet(vel20sp, acc05, ceroBugs);
    Set<Metric<?>> q3Metrics = asSet(vel20sp, acc02, ceroBugs);
    Set<Metric<?>> q4Metrics = asSet(vel20sp, acc01, ceroBugs);

    Set<MetricAggregation> expected = asSet(
        new MetricAggregation(asSet(partitionQ1, partitionPod1, partition2014),
            q1Metrics),
        new MetricAggregation(asSet(partitionQ1, partitionPod2, partition2014),
            q1Metrics),
        new MetricAggregation(asSet(partitionQ1, partitionPod3, partition2014),
            q1Metrics),
        new MetricAggregation(asSet(partitionQ2, partitionPod1, partition2014),
            q2Metrics),
        new MetricAggregation(asSet(partitionQ2, partitionPod2, partition2014),
            q2Metrics),
        new MetricAggregation(asSet(partitionQ2, partitionPod3, partition2014),
            q2Metrics),
        new MetricAggregation(asSet(partitionQ3, partitionPod1, partition2014),
            q3Metrics),
        new MetricAggregation(asSet(partitionQ3, partitionPod2, partition2014),
            q3Metrics),
        new MetricAggregation(asSet(partitionQ3, partitionPod3, partition2014),
            q3Metrics),
        new MetricAggregation(asSet(partitionQ4, partitionPod1, partition2014),
            q4Metrics),
        new MetricAggregation(asSet(partitionQ4, partitionPod2, partition2014),
            q4Metrics),
        new MetricAggregation(asSet(partitionQ4, partitionPod3, partition2014),
            q4Metrics));


    List<Partitioner<? extends Partition<?>>> input = Arrays.asList(
        new YearPartitioner(), new QuarterPartitioner(), new PodPartitioner());

    CollectionBasedQueryDslPredicateExecutor<SprintPodMetric> dataGenerator = createRepoMixin(
        QSprintPodMetric.sprintPodMetric, new DummyDataGenerator()
            .buildScenario1().getSprintMetrics());

    return new Object[] {input, expected, dataGenerator};
  }

  private static Object[] scenarioSprintPod() {
    Partition<String> partitionPod1 = new Partition<String>("pod",
        "scenario1-pod1");
    Partition<String> partitionPod2 = new Partition<String>("pod",
        "scenario1-pod2");
    Partition<String> partitionPod3 = new Partition<String>("pod",
        "scenario1-pod3");

    Metric<Integer> velocity10sp = new Metric<Integer>("velocity", 10,
        "story points");

    Metric<Double> acc01 = new Metric<Double>("accuracy-of-estimations", 0.1,
        "percentage");
    Metric<Double> acc02 = new Metric<Double>("accuracy-of-estimations", 0.2,
        "percentage");
    Metric<Double> acc05 = new Metric<Double>("accuracy-of-estimations", 0.5,
        "percentage");
    Metric<Double> acc1 = new Metric<Double>("accuracy-of-estimations", 1.0,
        "percentage");

    Partition<String> sprintQ1n1 = new Partition<String>("sprint",
        "scenario1-sprint-q1-1");
    Partition<String> sprintQ1n2 = new Partition<String>("sprint",
        "scenario1-sprint-q1-2");
    Partition<String> sprintQ2n1 = new Partition<String>("sprint",
        "scenario1-sprint-q2-1");
    Partition<String> sprintQ2n2 = new Partition<String>("sprint",
        "scenario1-sprint-q2-2");
    Partition<String> sprintQ3n1 = new Partition<String>("sprint",
        "scenario1-sprint-q3-1");
    Partition<String> sprintQ3n2 = new Partition<String>("sprint",
        "scenario1-sprint-q3-2");
    Partition<String> sprintQ4n1 = new Partition<String>("sprint",
        "scenario1-sprint-q4-1");
    Partition<String> sprintQ4n2 = new Partition<String>("sprint",
        "scenario1-sprint-q4-2");

    Metric<Integer> ceroBugs = new Metric<Integer>("bugs", 0, "number of bugs");

    Set<MetricAggregation> expected = asSet(
        new MetricAggregation(asSet(partitionPod1, sprintQ1n1), asSet(
            velocity10sp, acc1, ceroBugs)),
        new MetricAggregation(asSet(partitionPod1, sprintQ1n2), asSet(
            velocity10sp, acc1, ceroBugs)),
        new MetricAggregation(asSet(partitionPod1, sprintQ2n1), asSet(
            velocity10sp, acc05, ceroBugs)),
        new MetricAggregation(asSet(partitionPod1, sprintQ2n2), asSet(
            velocity10sp, acc05, ceroBugs)),
        new MetricAggregation(asSet(partitionPod1, sprintQ3n1), asSet(
            velocity10sp, acc02, ceroBugs)),
        new MetricAggregation(asSet(partitionPod1, sprintQ3n2), asSet(
            velocity10sp, acc02, ceroBugs)),
        new MetricAggregation(asSet(partitionPod1, sprintQ4n1), asSet(
            velocity10sp, acc01, ceroBugs)),
        new MetricAggregation(asSet(partitionPod1, sprintQ4n2), asSet(
            velocity10sp, acc01, ceroBugs)),
        new MetricAggregation(asSet(partitionPod2, sprintQ1n1), asSet(
            velocity10sp, acc1, ceroBugs)),
        new MetricAggregation(asSet(partitionPod2, sprintQ1n2), asSet(
            velocity10sp, acc1, ceroBugs)),
        new MetricAggregation(asSet(partitionPod2, sprintQ2n1), asSet(
            velocity10sp, acc05, ceroBugs)),
        new MetricAggregation(asSet(partitionPod2, sprintQ2n2), asSet(
            velocity10sp, acc05, ceroBugs)),
        new MetricAggregation(asSet(partitionPod2, sprintQ3n1), asSet(
            velocity10sp, acc02, ceroBugs)),
        new MetricAggregation(asSet(partitionPod2, sprintQ3n2), asSet(
            velocity10sp, acc02, ceroBugs)),
        new MetricAggregation(asSet(partitionPod2, sprintQ4n1), asSet(
            velocity10sp, acc01, ceroBugs)),
        new MetricAggregation(asSet(partitionPod2, sprintQ4n2), asSet(
            velocity10sp, acc01, ceroBugs)),
        new MetricAggregation(asSet(partitionPod3, sprintQ1n1), asSet(
            velocity10sp, acc1, ceroBugs)),
        new MetricAggregation(asSet(partitionPod3, sprintQ1n2), asSet(
            velocity10sp, acc1, ceroBugs)),
        new MetricAggregation(asSet(partitionPod3, sprintQ2n1), asSet(
            velocity10sp, acc05, ceroBugs)),
        new MetricAggregation(asSet(partitionPod3, sprintQ2n2), asSet(
            velocity10sp, acc05, ceroBugs)),
        new MetricAggregation(asSet(partitionPod3, sprintQ3n1), asSet(
            velocity10sp, acc02, ceroBugs)),
        new MetricAggregation(asSet(partitionPod3, sprintQ3n2), asSet(
            velocity10sp, acc02, ceroBugs)),
        new MetricAggregation(asSet(partitionPod3, sprintQ4n1), asSet(
            velocity10sp, acc01, ceroBugs)),
        new MetricAggregation(asSet(partitionPod3, sprintQ4n2), asSet(
            velocity10sp, acc01, ceroBugs)));


    List<Partitioner<? extends Partition<?>>> input = Arrays.asList(
        new SprintPartitioner(), new PodPartitioner());

    CollectionBasedQueryDslPredicateExecutor<SprintPodMetric> dataGenerator = createRepoMixin(
        QSprintPodMetric.sprintPodMetric, new DummyDataGenerator()
            .buildScenario1().getSprintMetrics());

    return new Object[] {input, expected, dataGenerator};
  }

  private static Object[] scenarioSprintPodBugs() {
    Partition<String> partitionPod1 = new Partition<String>("pod",
        "scenario3-pod1");
    Partition<String> partitionPod2 = new Partition<String>("pod",
        "scenario3-pod2");

    Metric<Integer> velocity0sp = new Metric<Integer>("velocity", 0,
        "story points");

    Metric<Double> accNaN = new Metric<Double>("accuracy-of-estimations",
        Double.NaN, "percentage");

    Partition<String> sprint1 = new Partition<String>("sprint",
        "scenario3-sprint-1");
    Partition<String> sprint2 = new Partition<String>("sprint",
        "scenario3-sprint-2");

    Metric<Integer> bugs1 = new Metric<Integer>("bugs", 1, "number of bugs");
    Metric<Integer> bugs2 = new Metric<Integer>("bugs", 2, "number of bugs");
    Metric<Integer> bugs3 = new Metric<Integer>("bugs", 3, "number of bugs");
    Metric<Integer> bugs4 = new Metric<Integer>("bugs", 4, "number of bugs");

    Set<MetricAggregation> expected = asSet(
        new MetricAggregation(asSet(partitionPod1, sprint1), asSet(velocity0sp,
            accNaN, bugs1)),
        new MetricAggregation(asSet(partitionPod1, sprint2), asSet(velocity0sp,
            accNaN, bugs2)),
        new MetricAggregation(asSet(partitionPod2, sprint1), asSet(velocity0sp,
            accNaN, bugs3)), new MetricAggregation(
            asSet(partitionPod2, sprint2), asSet(velocity0sp, accNaN, bugs4)));

    List<Partitioner<? extends Partition<?>>> input = Arrays.asList(
        new SprintPartitioner(), new PodPartitioner());

    CollectionBasedQueryDslPredicateExecutor<SprintPodMetric> dataGenerator = createRepoMixin(
        QSprintPodMetric.sprintPodMetric, new DummyDataGenerator()
            .buildScenario3().getSprintMetrics());

    return new Object[] {input, expected, dataGenerator};
  }


  @SafeVarargs
  private static <T> Set<T> asSet(T... elements) {
    return new HashSet<T>(Arrays.asList(elements));
  }
}
