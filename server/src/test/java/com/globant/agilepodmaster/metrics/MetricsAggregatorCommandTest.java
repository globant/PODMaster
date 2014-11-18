package com.globant.agilepodmaster.metrics;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.globant.agilepodmaster.core.DummyDataSprintPodMetricRepository;
import com.globant.agilepodmaster.core.Quarter;
import com.globant.agilepodmaster.core.SprintPodMetric;
import com.globant.agilepodmaster.core.SprintPodMetricRepository;
import com.globant.agilepodmaster.metrics.partition.Partition;
import com.globant.agilepodmaster.metrics.partition.Partitioner;
import com.globant.agilepodmaster.metrics.partition.PodPartitioner;
import com.globant.agilepodmaster.metrics.partition.QuarterPartitioner;
import com.globant.agilepodmaster.metrics.partition.SprintPartitioner;
import com.globant.agilepodmaster.metrics.partition.YearPartitioner;


@RunWith(Parameterized.class)
public class MetricsAggregatorCommandTest {
  private Set<MetricAggregation> expectedResult; 
  private List<Partitioner<SprintPodMetric, ? extends Partition<?>>> partitioners;

  public MetricsAggregatorCommandTest(
      List<Partitioner<SprintPodMetric, ? extends Partition<?>>> partitioners,
      Set<MetricAggregation> expectedResult) {
    this.expectedResult = expectedResult;
    this.partitioners = partitioners;
  }

  @Test
  public void test() {
    MetricsAggregatorCommand command = createCommand();

    Set<MetricAggregation> aggregations = command.execute(partitioners, null);
    System.out.println("---");
    aggregations.forEach(it -> System.out.println(it));
    assertThat(aggregations, equalTo(expectedResult));
  }

  private MetricsAggregatorCommand createCommand() {
    return new MetricsAggregatorCommand(new MetricsAggregator(), createDummyRepo());
  }

  private SprintPodMetricRepository createDummyRepo() {
    return new DummyDataSprintPodMetricRepository();
  }

  @Parameters
  public static Collection<Object[]> testData() {
    return Arrays.asList(new Object[][] { 
        scenarioEmptyPartitionersList(),
        scenarioYearQuarterPod(),
        scenarioSprintPod(),
        scenarioPod()
    });
  }
  
  private static Object[] scenarioEmptyPartitionersList() {    
    Metric<Integer> velocity240sp = new Metric<Integer>("velocity", 240, "story points");
    
    Set<MetricAggregation> expected = asSet(
        new MetricAggregation(new HashSet<Partition<?>>(), asSet(velocity240sp))
    );
    
    List<Partitioner<SprintPodMetric, ? extends Partition<?>>> input = 
        new ArrayList<Partitioner<SprintPodMetric, ? extends Partition<?>>>();
    
    return new Object[] { input, expected };
  }
  
  private static Object[] scenarioPod() {
    Partition<String> partitionPod1 = new Partition<String>("pod", "pod1");
    Partition<String> partitionPod2 = new Partition<String>("pod", "pod2");
    Partition<String> partitionPod3 = new Partition<String>("pod", "pod3");
    
    Metric<Integer> velocity80sp = new Metric<Integer>("velocity", 80, "story points");
    
    Set<MetricAggregation> expected = asSet(
        new MetricAggregation(asSet(partitionPod1), asSet(velocity80sp)),
        new MetricAggregation(asSet(partitionPod2), asSet(velocity80sp)),
        new MetricAggregation(asSet(partitionPod3), asSet(velocity80sp))
    );
    
    
    List<Partitioner<SprintPodMetric, ? extends Partition<?>>> input = Arrays.asList(
        new PodPartitioner()
    );
    
    return new Object[] { input, expected };
  }
  
  private static Object[] scenarioYearQuarterPod() {
    Partition<Quarter> partitionQ1 = new Partition<Quarter>("quarter", Quarter.Q1);
    Partition<Quarter> partitionQ2 = new Partition<Quarter>("quarter", Quarter.Q2);
    Partition<Quarter> partitionQ3 = new Partition<Quarter>("quarter", Quarter.Q3);
    Partition<Quarter> partitionQ4 = new Partition<Quarter>("quarter", Quarter.Q4);
    
    Partition<String> partitionPod1 = new Partition<String>("pod", "pod1");
    Partition<String> partitionPod2 = new Partition<String>("pod", "pod2");
    Partition<String> partitionPod3 = new Partition<String>("pod", "pod3");
    
    Partition<Integer> partition2014 = new Partition<Integer>("year", 2014);
    
    Metric<Integer> vel20sp = new Metric<Integer>("velocity", 20, "story points");
    
    Set<MetricAggregation> expected = asSet(
        new MetricAggregation(asSet(partitionQ1, partitionPod1, partition2014), asSet(vel20sp)),
        new MetricAggregation(asSet(partitionQ1, partitionPod2, partition2014), asSet(vel20sp)),
        new MetricAggregation(asSet(partitionQ1, partitionPod3, partition2014), asSet(vel20sp)),
        new MetricAggregation(asSet(partitionQ2, partitionPod1, partition2014), asSet(vel20sp)),
        new MetricAggregation(asSet(partitionQ2, partitionPod2, partition2014), asSet(vel20sp)),
        new MetricAggregation(asSet(partitionQ2, partitionPod3, partition2014), asSet(vel20sp)),
        new MetricAggregation(asSet(partitionQ3, partitionPod1, partition2014), asSet(vel20sp)),
        new MetricAggregation(asSet(partitionQ3, partitionPod2, partition2014), asSet(vel20sp)),
        new MetricAggregation(asSet(partitionQ3, partitionPod3, partition2014), asSet(vel20sp)),
        new MetricAggregation(asSet(partitionQ4, partitionPod1, partition2014), asSet(vel20sp)),
        new MetricAggregation(asSet(partitionQ4, partitionPod2, partition2014), asSet(vel20sp)),
        new MetricAggregation(asSet(partitionQ4, partitionPod3, partition2014), asSet(vel20sp))
    );
    
    
    List<Partitioner<SprintPodMetric, ? extends Partition<?>>> input = Arrays.asList(
        new YearPartitioner(), new QuarterPartitioner(), new PodPartitioner());
    
    return new Object[] { input, expected };
  }
  
  private static Object[] scenarioSprintPod() {
    Partition<String> partitionPod1 = new Partition<String>("pod", "pod1");
    Partition<String> partitionPod2 = new Partition<String>("pod", "pod2");
    Partition<String> partitionPod3 = new Partition<String>("pod", "pod3");
    
    Metric<Integer> velocity10sp = new Metric<Integer>("velocity", 10, "story points");
    
    Partition<String> sprintQ1n1 = new Partition<String>("sprint", "sprint-q1-1");
    Partition<String> sprintQ1n2 = new Partition<String>("sprint", "sprint-q1-2");
    Partition<String> sprintQ2n1 = new Partition<String>("sprint", "sprint-q2-1");
    Partition<String> sprintQ2n2 = new Partition<String>("sprint", "sprint-q2-2");
    Partition<String> sprintQ3n1 = new Partition<String>("sprint", "sprint-q3-1");
    Partition<String> sprintQ3n2 = new Partition<String>("sprint", "sprint-q3-2");
    Partition<String> sprintQ4n1 = new Partition<String>("sprint", "sprint-q4-1");
    Partition<String> sprintQ4n2 = new Partition<String>("sprint", "sprint-q4-2");

    Set<MetricAggregation> expected = asSet(
        new MetricAggregation(asSet(partitionPod1, sprintQ1n1), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod1, sprintQ1n2), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod1, sprintQ2n1), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod1, sprintQ2n2), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod1, sprintQ3n1), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod1, sprintQ3n2), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod1, sprintQ4n1), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod1, sprintQ4n2), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod2, sprintQ1n1), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod2, sprintQ1n2), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod2, sprintQ2n1), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod2, sprintQ2n2), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod2, sprintQ3n1), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod2, sprintQ3n2), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod2, sprintQ4n1), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod2, sprintQ4n2), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod3, sprintQ1n1), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod3, sprintQ1n2), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod3, sprintQ2n1), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod3, sprintQ2n2), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod3, sprintQ3n1), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod3, sprintQ3n2), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod3, sprintQ4n1), asSet(velocity10sp)),
        new MetricAggregation(asSet(partitionPod3, sprintQ4n2), asSet(velocity10sp))
    );

    
    List<Partitioner<SprintPodMetric, ? extends Partition<?>>> input = Arrays.asList(
        new SprintPartitioner(), new PodPartitioner());
    
    return new Object[] { input, expected };
  }
  
  @SafeVarargs
  private static <T> Set<T> asSet(T... elements) {
    return new HashSet<T>(Arrays.asList(elements));
  }
}
