package com.globant.agilepodmaster.metrics;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import com.globant.agilepodmaster.metrics.partition.Partition;
import com.globant.agilepodmaster.metrics.partition.Partitioner;
import com.globant.agilepodmaster.metrics.partition.PodPartitioner;
import com.globant.agilepodmaster.metrics.partition.QuarterPartitioner;
import com.globant.agilepodmaster.metrics.partition.SprintPartitioner;
import com.globant.agilepodmaster.metrics.partition.YearPartitioner;
import com.globant.agilepodmaster.snapshot.AbstractMetric;
import com.globant.agilepodmaster.snapshot.AbstractMetricRepository;
import com.globant.agilepodmaster.snapshot.ProjectMetric;
import com.globant.agilepodmaster.snapshot.QProjectMetric;
import com.globant.agilepodmaster.snapshot.QSprintPodMetric;
import com.globant.agilepodmaster.snapshot.SprintPodMetric;
import com.mysema.query.types.path.EntityPathBase;

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
import java.util.function.Function;
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
  private  List<Function<List<AbstractMetric>, Metric<?>>> collectList;

  /**
   * Constructor.
   * 
   * @param partitioners input partitioners.
   * @param expectedResult expected results.
   * @param repo repository to be used.
   * @param collectList list of metrics to be shown.
   */
  public MetricsAggregatorCommandTest(
      List<Partitioner<? extends Partition<?>>> partitioners,
      Set<MetricAggregation> expectedResult, Object repo,
      List<Function<List<AbstractMetric>, Metric<?>>> collectList) {
    this.expectedResult = expectedResult;
    this.partitioners = partitioners;
    this.repo = repo;
    this.collectList = collectList;
  }

  /**
   * Testing with different parameters.
   */
  @Test
  public void test() {
    MetricsAggregatorCommand command = new MetricsAggregatorCommand(
        new MetricsAggregator(), createDummyRepo(repo,
            AbstractMetricRepository.class));

    Set<MetricAggregation> aggregations = command.execute(partitioners, null, collectList);

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
        scenarioEmptyPartitionersList(), 
        scenarioYearQuarterPod(),
        scenarioSprintPod(), 
        scenarioSprintPodBugs(), 
        scenarioPod(),

        // ProjectMetric scenarios
        projectMetricsScenarioEmptyPartitionersList(),
        projectMetricsScenarioYearQuarter(), 
        projectMetricsScenarioPod()});
  }

  private static Object[] projectMetricsScenarioYearQuarter() {   
    
    Metric<Integer> remaining = new Metric<Integer>("remaining-story-points",
        80, "story points");
    
    Metric<Double> actualPercentComplete = new Metric<Double>("actual-percent-complete",
        0.2, "percentage");

    Set<MetricAggregation> expected = asSet(new MetricAggregation(
        new HashSet<Partition<?>>(), asSet(remaining, actualPercentComplete)));

    List<Partitioner<? extends Partition<?>>> input = Arrays.asList(
        new YearPartitioner(), new QuarterPartitioner());

    Set<ProjectMetric> projectMetrics = new DummyDataGenerator()
        .buildScenario2().getProjectMetrics();
    CollectionBasedQueryDslPredicateExecutor<ProjectMetric> dataGenerator = createRepoMixin(
        QProjectMetric.projectMetric, projectMetrics);

    return new Object[] {input, expected, dataGenerator, null};
  }

  private static Object[] projectMetricsScenarioEmptyPartitionersList() {
    
    Metric<Integer> remaining = new Metric<Integer>("remaining-story-points",
        80, "story points");
    
    Metric<Double> actualPercentComplete = new Metric<Double>("actual-percent-complete",
        0.2, "percentage");


    Set<MetricAggregation> expected = asSet(new MetricAggregation(
        new HashSet<Partition<?>>(), asSet(remaining, actualPercentComplete)));

    List<Partitioner<? extends Partition<?>>> input = 
        new ArrayList<Partitioner<? extends Partition<?>>>();

    Set<ProjectMetric> projectMetrics = new DummyDataGenerator()
        .buildScenario2().getProjectMetrics();
    CollectionBasedQueryDslPredicateExecutor<ProjectMetric> dataGenerator = createRepoMixin(
        QProjectMetric.projectMetric, projectMetrics);
    
    return new Object[] {
        input, expected, dataGenerator,
        Arrays.asList( MetricsAggregator.metricCalculatorMap.get("remaining-story-points"), 
            MetricsAggregator.metricCalculatorMap.get("actual-percent-complete"))};
  }

  private static Object[] projectMetricsScenarioPod() {

    Metric<Integer> remaining = new Metric<Integer>("remaining-story-points",
        80, "story points");
    
    Metric<Double> actualPercentComplete = new Metric<Double>("actual-percent-complete",
        0.2, "percentage");

    Set<MetricAggregation> expected = asSet(new MetricAggregation(
        new HashSet<Partition<?>>(), asSet(remaining, actualPercentComplete)));

    List<Partitioner<? extends Partition<?>>> input = Arrays
        .asList(new PodPartitioner());

    Set<ProjectMetric> projectMetrics = new DummyDataGenerator()
        .buildScenario2().getProjectMetrics();
    CollectionBasedQueryDslPredicateExecutor<ProjectMetric> dataGenerator = createRepoMixin(
        QProjectMetric.projectMetric, projectMetrics);

    return new Object[] {input, expected, dataGenerator, null};
  }

  private static Object[] scenarioEmptyPartitionersList() {
    Metric<Integer> velocity1080sp = new Metric<Integer>("velocity", 1080,
        "story points");

    Set<MetricAggregation> expected = asSet(new MetricAggregation(
        new HashSet<Partition<?>>(), asSet(velocity1080sp)));

    List<Partitioner<? extends Partition<?>>> input = 
        new ArrayList<Partitioner<? extends Partition<?>>>();

    CollectionBasedQueryDslPredicateExecutor<SprintPodMetric> dataGenerator = createRepoMixin(
        QSprintPodMetric.sprintPodMetric, new DummyDataGenerator()
            .buildScenario1().getSprintPodMetrics());

    
    return new Object[] {
        input, expected, dataGenerator,
        Arrays.asList( MetricsAggregator.metricCalculatorMap.get("velocity"))};
  }

  private static Object[] scenarioPod() {
    Partition<String> partitionPod1 = new Partition<String>("pod",
        "scenario1-pod1");
    Partition<String> partitionPod2 = new Partition<String>("pod",
        "scenario1-pod2");
    Partition<String> partitionPod3 = new Partition<String>("pod",
        "scenario1-pod3");

    Metric<Integer> velocity360sp = new Metric<Integer>("velocity", 360,
        "story points");
    
    Metric<Double> sov = new Metric<Double>("stability-of-velocity", 0.4539933184772378,
        "percentage");
    
    Set<MetricAggregation> expected = asSet(
        new MetricAggregation(asSet(partitionPod1), asSet(velocity360sp, sov)),
        new MetricAggregation(asSet(partitionPod2), asSet(velocity360sp, sov)),
        new MetricAggregation(asSet(partitionPod3), asSet(velocity360sp, sov)));

    List<Partitioner<? extends Partition<?>>> input = Arrays
        .asList(new PodPartitioner());

    CollectionBasedQueryDslPredicateExecutor<SprintPodMetric> dataGenerator = createRepoMixin(
        QSprintPodMetric.sprintPodMetric, new DummyDataGenerator()
            .buildScenario1().getSprintPodMetrics());

    return new Object[] {
        input, expected, dataGenerator,
        Arrays.asList( MetricsAggregator.metricCalculatorMap.get("velocity"), 
            MetricsAggregator.metricCalculatorMap.get("stability-of-velocity"))};
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
    
    Metric<Integer> vel40sp = new Metric<Integer>("velocity", 40,
        "story points");
    
    Metric<Integer> vel100sp = new Metric<Integer>("velocity", 100,
        "story points");
    
    Metric<Integer> vel200sp = new Metric<Integer>("velocity", 200,
        "story points");

    Set<Metric<?>> q1Metrics = asSet(vel20sp);
    Set<Metric<?>> q2Metrics = asSet(vel40sp);
    Set<Metric<?>> q3Metrics = asSet(vel100sp);
    Set<Metric<?>> q4Metrics = asSet(vel200sp);

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
            .buildScenario1().getSprintPodMetrics());

    return new Object[] {
        input, expected, dataGenerator,
        Arrays.asList(MetricsAggregator.metricCalculatorMap.get("velocity"))};
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
    Metric<Integer> velocity20sp = new Metric<Integer>("velocity", 20,
        "story points");
    Metric<Integer> velocity50sp = new Metric<Integer>("velocity", 50,
        "story points");
    Metric<Integer> velocity100sp = new Metric<Integer>("velocity", 100,
        "story points");

    Metric<Integer> acc10sp = new Metric<Integer>("accumulated-story-points",
        10, "story points");
    Metric<Integer> acc20sp = new Metric<Integer>("accumulated-story-points",
        20, "story points");
    Metric<Integer> acc40sp = new Metric<Integer>("accumulated-story-points",
        40, "story points");
    Metric<Integer> acc60sp = new Metric<Integer>("accumulated-story-points",
        60, "story points");
    Metric<Integer> acc110sp = new Metric<Integer>("accumulated-story-points",
        110, "story points");
    Metric<Integer> acc160sp = new Metric<Integer>("accumulated-story-points",
        160, "story points");
    Metric<Integer> acc260sp = new Metric<Integer>("accumulated-story-points",
        260, "story points");
    Metric<Integer> acc360sp = new Metric<Integer>("accumulated-story-points",
        360, "story points");

    Partition<Integer> sprintQ1n1 = new Partition<Integer>("sprint", 1);
    Partition<Integer> sprintQ1n2 = new Partition<Integer>("sprint", 2);
    Partition<Integer> sprintQ2n1 = new Partition<Integer>("sprint", 3);
    Partition<Integer> sprintQ2n2 = new Partition<Integer>("sprint", 4);
    Partition<Integer> sprintQ3n1 = new Partition<Integer>("sprint", 5);
    Partition<Integer> sprintQ3n2 = new Partition<Integer>("sprint", 6);
    Partition<Integer> sprintQ4n1 = new Partition<Integer>("sprint", 7);
    Partition<Integer> sprintQ4n2 = new Partition<Integer>("sprint", 8);

    Set<MetricAggregation> expected = asSet(
        new MetricAggregation(asSet(partitionPod1, sprintQ1n1), asSet(
            velocity10sp, acc10sp)),
        new MetricAggregation(asSet(partitionPod1, sprintQ1n2), asSet(
            velocity10sp, acc20sp)),
        new MetricAggregation(asSet(partitionPod1, sprintQ2n1), asSet(
            velocity20sp, acc40sp)),
        new MetricAggregation(asSet(partitionPod1, sprintQ2n2), asSet(
            velocity20sp, acc60sp)),
        new MetricAggregation(asSet(partitionPod1, sprintQ3n1), asSet(
            velocity50sp, acc110sp)),
        new MetricAggregation(asSet(partitionPod1, sprintQ3n2), asSet(
            velocity50sp, acc160sp)),
        new MetricAggregation(asSet(partitionPod1, sprintQ4n1), asSet(
            velocity100sp, acc260sp)),
        new MetricAggregation(asSet(partitionPod1, sprintQ4n2), asSet(
            velocity100sp, acc360sp)),
        new MetricAggregation(asSet(partitionPod2, sprintQ1n1), asSet(
            velocity10sp, acc10sp)),
        new MetricAggregation(asSet(partitionPod2, sprintQ1n2), asSet(
            velocity10sp, acc20sp)),
        new MetricAggregation(asSet(partitionPod2, sprintQ2n1), asSet(
            velocity20sp, acc40sp)),
        new MetricAggregation(asSet(partitionPod2, sprintQ2n2), asSet(
            velocity20sp, acc60sp)),
        new MetricAggregation(asSet(partitionPod2, sprintQ3n1), asSet(
            velocity50sp, acc110sp)),
        new MetricAggregation(asSet(partitionPod2, sprintQ3n2), asSet(
            velocity50sp, acc160sp)),
        new MetricAggregation(asSet(partitionPod2, sprintQ4n1), asSet(
            velocity100sp, acc260sp)),
        new MetricAggregation(asSet(partitionPod2, sprintQ4n2), asSet(
            velocity100sp, acc360sp)),
        new MetricAggregation(asSet(partitionPod3, sprintQ1n1), asSet(
            velocity10sp, acc10sp)),
        new MetricAggregation(asSet(partitionPod3, sprintQ1n2), asSet(
            velocity10sp, acc20sp)),
        new MetricAggregation(asSet(partitionPod3, sprintQ2n1), asSet(
            velocity20sp, acc40sp)),
        new MetricAggregation(asSet(partitionPod3, sprintQ2n2), asSet(
            velocity20sp, acc60sp)),
        new MetricAggregation(asSet(partitionPod3, sprintQ3n1), asSet(
            velocity50sp, acc110sp)),
        new MetricAggregation(asSet(partitionPod3, sprintQ3n2), asSet(
            velocity50sp, acc160sp)),
        new MetricAggregation(asSet(partitionPod3, sprintQ4n1), asSet(
            velocity100sp, acc260sp)),
        new MetricAggregation(asSet(partitionPod3, sprintQ4n2), asSet(
            velocity100sp, acc360sp)));

    List<Partitioner<? extends Partition<?>>> input = Arrays.asList(
        new SprintPartitioner(), new PodPartitioner());

    CollectionBasedQueryDslPredicateExecutor<SprintPodMetric> dataGenerator = createRepoMixin(
        QSprintPodMetric.sprintPodMetric, new DummyDataGenerator()
            .buildScenario1().getSprintPodMetrics());

    return new Object[] {
        input, expected, dataGenerator,
        Arrays.asList(MetricsAggregator.metricCalculatorMap.get("velocity"), 
            MetricsAggregator.metricCalculatorMap.get("accumulated-story-points"))};
  }

  private static Object[] scenarioSprintPodBugs() {
    Partition<String> partitionPod1 = new Partition<String>("pod",
        "scenario3-pod1");
    Partition<String> partitionPod2 = new Partition<String>("pod",
        "scenario3-pod2");

    Partition<Integer> sprint1 = new Partition<Integer>("sprint", 1);
    Partition<Integer> sprint2 = new Partition<Integer>("sprint", 2);

    Metric<Integer> bugs1 = new Metric<Integer>("bugs", 1, "number");
    Metric<Integer> bugs2 = new Metric<Integer>("bugs", 2, "number");
    Metric<Integer> bugs3 = new Metric<Integer>("bugs", 3, "number");
    Metric<Integer> bugs4 = new Metric<Integer>("bugs", 4, "number");

    Set<MetricAggregation> expected = asSet(
        new MetricAggregation(asSet(partitionPod1, sprint1), asSet(bugs1)),
        new MetricAggregation(asSet(partitionPod1, sprint2), asSet(bugs2)),
        new MetricAggregation(asSet(partitionPod2, sprint1), asSet(bugs3)), 
        new MetricAggregation(asSet(partitionPod2, sprint2), asSet(bugs4)));

    List<Partitioner<? extends Partition<?>>> input = Arrays.asList(
        new SprintPartitioner(), new PodPartitioner());

    CollectionBasedQueryDslPredicateExecutor<SprintPodMetric> dataGenerator = createRepoMixin(
        QSprintPodMetric.sprintPodMetric, new DummyDataGenerator()
            .buildScenario3().getSprintPodMetrics());

    return new Object[] {
        input, expected, dataGenerator,
        Arrays.asList(MetricsAggregator.metricCalculatorMap.get("bugs"))};
  }


  @SafeVarargs
  private static <T> Set<T> asSet(T... elements) {
    return new HashSet<T>(Arrays.asList(elements));
  }
}
