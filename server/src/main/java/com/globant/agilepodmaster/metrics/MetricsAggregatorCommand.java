package com.globant.agilepodmaster.metrics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.core.SprintPodMetric;
import com.globant.agilepodmaster.core.SprintPodMetricRepository;
import com.globant.agilepodmaster.metrics.partition.Partition;
import com.globant.agilepodmaster.metrics.partition.Partitioner;
import com.mysema.query.types.Predicate;

@Component
public class MetricsAggregatorCommand {
  private MetricsAggregator aggregator;
  private SprintPodMetricRepository repo;

  @Autowired
  public MetricsAggregatorCommand(MetricsAggregator aggregator, SprintPodMetricRepository repo) {
    this.aggregator = aggregator;
    this.repo = repo;
  }

  public Set<MetricAggregation> execute(
      List<Partitioner<SprintPodMetric, ? extends Partition<?>>> partitioners, Predicate filters) {

    Iterable<SprintPodMetric> spmList = repo.findAll(filters);

    Map<Set<Partition<?>>, List<SprintPodMetric>> partitions = createPartitions(
        spmList, partitioners);
    
    Set<MetricAggregation> aggregated = this.aggregate(partitions);

    return aggregated;
  }

  private Set<MetricAggregation> aggregate(Map<Set<Partition<?>>, List<SprintPodMetric>> parts) {

    Set<MetricAggregation> aggregated = new HashSet<MetricAggregation>();
    parts.forEach(
        (part, lst) -> aggregated.add(new MetricAggregation(part, aggregator.aggregate(lst)))
    );

    return aggregated;
  }

  private SprintPodMetricPartitionsMap createPartitions(
      Iterable<SprintPodMetric> spmList,
      List<Partitioner<SprintPodMetric, ? extends Partition<?>>> partitioners) {

    SprintPodMetricPartitionsMap partitions = new SprintPodMetricPartitionsMap();
    spmList.forEach(spm -> partitions.get(createPartitions(spm, partitioners)).add(spm));

    return partitions;
  }

  private Set<Partition<?>> createPartitions(
      SprintPodMetric spm,
      List<Partitioner<SprintPodMetric, ? extends Partition<?>>> partitioners) {

    Set<Partition<?>> partition = new HashSet<Partition<?>>();
    partitioners.forEach(p -> partition.add(p.extractPartition(spm)));

    return partition;
  }
}

@SuppressWarnings("serial")
class SprintPodMetricPartitionsMap extends HashMap<Set<Partition<?>>, List<SprintPodMetric>> {
  @SuppressWarnings("unchecked")
  @Override
  public List<SprintPodMetric> get(Object key) {
    this.putIfAbsent((Set<Partition<?>>) key, new LinkedList<SprintPodMetric>());
    return super.get(key);
  }
}
