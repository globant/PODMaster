package com.globant.agilepodmaster.metrics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.core.AbstractMetric;
import com.globant.agilepodmaster.core.AbstractMetricRepository;
import com.globant.agilepodmaster.metrics.partition.Partition;
import com.globant.agilepodmaster.metrics.partition.Partitioner;
import com.mysema.query.types.Predicate;

/**
 * Command that returns metrics.
 * @author Andres Postiglioni.
 *
 */
@Component
public class MetricsAggregatorCommand {
  private MetricsAggregator aggregator;
  private AbstractMetricRepository repo;

  /**
   * Constructor.
   * @param aggregator service to the the metric values.
   * @param repo repo to get the metrics.
   */
  @Autowired
  public MetricsAggregatorCommand(
      MetricsAggregator aggregator, 
      AbstractMetricRepository repo) {
    
    this.aggregator = aggregator;
    this.repo = repo;
  }

  /**
   * Executes the command.
   * @param partitioners the partitioners.
   * @param filters the filters to be applied.
   * @return a set of aggregations.
   */
  public Set<MetricAggregation> execute(
      List<Partitioner<? extends Partition<?>>> partitioners, 
      Predicate filters) {

//    Iterable<AbstractMetric> spmList = findMetrics(filters);
    Iterable<AbstractMetric> spmList = repo.findAll(filters);

    Map<Set<Partition<?>>, List<AbstractMetric>> partitions = createPartitions(
        spmList, partitioners);
    
    Set<MetricAggregation> aggregated = this.aggregate(partitions);

    return aggregated;
  }

  private Set<MetricAggregation> aggregate(Map<Set<Partition<?>>, List<AbstractMetric>> parts) {

    Set<MetricAggregation> aggregated = new HashSet<MetricAggregation>();
    parts.forEach(
        (part, lst) -> aggregated.add(new MetricAggregation(part, aggregator.aggregate(lst)))
    );

    return aggregated;
  }

  /**
   * Creates a map that has as key a set of partitions and as value a list of
   * AbstractMetric that matches those partitions.
   * 
   * @param spmList list of AbstractMetric.
   * @param partitioners all possible partitioners.
   * @return the map.
   */
  private SprintPodMetricPartitionsMap createPartitions(
      Iterable<? extends AbstractMetric> spmList,
      List<Partitioner<? extends Partition<?>>> partitioners) {

    SprintPodMetricPartitionsMap partitions = new SprintPodMetricPartitionsMap();
    spmList.forEach(spm -> partitions.get(createPartitions(spm, partitioners))
        .add(spm));

    return partitions;
  }

  /**
   * Extract a set of partitions (Pod, Sprint, Year .. ) from an AbstractMetric.
   * @param data AbstractMetric object.
   * @param partitioners 
   * @return a set of partitions.
   */
  private Set<Partition<?>> createPartitions(
      AbstractMetric data,
      List<Partitioner<? extends Partition<?>>> partitioners) {

    Set<Partition<?>> partitions = new HashSet<Partition<?>>();
    
    for ( Partitioner<? extends Partition<?>> partitioner: partitioners) {
      Partition<?> partition = data.visit(partitioner);
      
      if (partition != null) {
        partitions.add(partition);
      }
    }

    return partitions;
  }
}

@SuppressWarnings("serial")
class SprintPodMetricPartitionsMap extends HashMap<Set<Partition<?>>, List<AbstractMetric>> {
  @SuppressWarnings("unchecked")
  @Override
  public List<AbstractMetric> get(Object key) {
    this.putIfAbsent((Set<Partition<?>>) key, new LinkedList<AbstractMetric>());
    return super.get(key);
  }
}
